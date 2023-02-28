#pragma clang diagnostic push
#pragma ide diagnostic ignored "UnusedLocalVariable"
#pragma ide diagnostic ignored "cert-err58-cpp"

#include "PythonThread.hpp"
#include "util.hpp"

#include "py_helpers/python_processing.hpp"
#include "Python.h"
#include "pythonrun.h"

#include <android/log.h>
#include <cstdio>
#include <fcntl.h>
#include <pthread.h>


// This is the object we use to call a specific method in a python file.
static py_helper::PythonProcessing mPyProcess;

void startStdErrLogging();

void startStdOutLogging(JNIEnv *pEnv, jobject pJobject);

static int mErrFile[2];
static int mOutFile[2];
static pthread_t mErrThread = -1;
static pthread_t mOutThread = -1;

void *out_thread_func(void *pVoid);

static void *err_thread_func(void *);

using namespace std;
JNIEnv *enV;
jobject obJ;
JavaVM *jvM;
bool statusResult = false;
bool statusErrorResult = false;
bool status = false;
bool statusError = false;





string errorMessage;
string mymessage;
JNI_obj jniObj;




void PyLogs::LOG(string message) {
    errorMessage = message;
    __android_log_write(ANDROID_LOG_VERBOSE, __FUNCTION__, errorMessage.c_str());


};

extern "C" {
JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM *jvm, void *reserved) {
    __android_log_write(ANDROID_LOG_VERBOSE, __FUNCTION__, "JNI_OnLoad");

    return JNI_VERSION_1_6;
}


JNIEXPORT jint JNICALL Java_com_example_testedit_PythonThread_initPython
        (JNIEnv *env, jobject obj, jstring aPath) {
    jniObj.env=env;
    jniObj.obj=obj;
    wstring lPassedPath = Utilities::getWStringFromJava(env, aPath);

    string lDirectory;

    lDirectory = Utilities::convertWChar(lPassedPath);

    if (!Utilities::dirExists(lDirectory)) {
        // Unable to initialize Python because the path does not exist
        __android_log_write(ANDROID_LOG_ERROR, __FUNCTION__,
                            "Python path does not exist. Make sure you copy it from the assets! ");
        return -1;
    }

    // We need to add this lib-dynload so that we have all our .so libraries available.
    lDirectory += "/lib-dynload";

    // Now make sure the SO directory exists too
    if (!Utilities::dirExists(lDirectory)) {
        // Unable to initialize Python because the path does not exist
        __android_log_write(ANDROID_LOG_ERROR, __FUNCTION__,
                            "Python lib-dynload path does not exist. Make sure you copy it from the assets!");
        return -1;
    }

    // Based on the string passed in lets setup our Python Path
    wstring lPyPath;
    lPyPath = lPassedPath + L"/:" + lPassedPath +
              L"/lib-dynload";      // This is our path, where Python is, and where the shared libraries are

    // Tell Python our path
    Py_SetPath(lPyPath.c_str());

    __android_log_write(ANDROID_LOG_VERBOSE, __FUNCTION__, "Leaving initPython");
    return 0;
}


JNIEXPORT jint JNICALL Java_com_example_testedit_PythonThread_cleanupPython
        (JNIEnv *env, jobject obj) {
    jniObj.env=env;
    jniObj.obj=obj;
    __android_log_write(ANDROID_LOG_VERBOSE, __FUNCTION__, "We are in Cleanup Python");

    // TODO: -- In a perfect world we setup our logging threads better to be able to shut down. We would set a flag and
    //  that would break the endless loop.

    //If we can't init, handle it
    if (!Py_IsInitialized()) {
        __android_log_write(ANDROID_LOG_ERROR, __FUNCTION__, "Python has not been initialized");
        return -4;
    }

    // Just call Finalize and be done
    __android_log_write(ANDROID_LOG_INFO, __FUNCTION__, "About to call finalize in Cleanup Python");
    Py_Finalize();

    __android_log_write(ANDROID_LOG_INFO, __FUNCTION__, "We are leaving Cleanup Python");
    return 1;
}

JNIEXPORT jint JNICALL Java_com_example_testedit_PythonThread_runPython
        (JNIEnv *env, jobject obj, jstring filename) {
    //enV->GetJavaVM(&jvM);
    enV = env;
    obJ = env->NewGlobalRef(obj);
    jniObj.env=env;
    jniObj.obj=obj;

    //env->SetLongField(obj, getPtrFieldId(env, obj), (jlong) new CppObject);



    //enV = env;
    //obJ = obj;


    __android_log_write(ANDROID_LOG_VERBOSE, __FUNCTION__, "We are in Run Python");

    // Initialize Python, we only want to do this once!
    Py_Initialize();

    //If we can't init, handle it
    if (!Py_IsInitialized()) {
        __android_log_write(ANDROID_LOG_ERROR, __FUNCTION__,
                            "Python has not been initialized while trying to start python in Run");
        return -4;
    } else {
        __android_log_write(ANDROID_LOG_VERBOSE, __FUNCTION__,
                            "Python Engine has been initialized");
    }

    string lPythonFile = Utilities::getStringFromJava(env,
                                                      filename);//преоброзуем java строку на С++ строку

    FILE *file;

    if (!Utilities::fileExists(lPythonFile)) {
        // The file does not exist, log a message and bail
        __android_log_write(ANDROID_LOG_ERROR, __FUNCTION__,
                            "The Python file could not be found. Please verify it has been installed.");
        return -1;
    }

    // Execute the python script.
    //long lLoadReturn = mPyProcess.loadFile(lPythonFile);
    mPyProcess.loadFile(lPythonFile);
    long lExecuteReturn = mPyProcess.executeFunction("main");

    mPyProcess.unloadFile();

    // Startup the Error logging
    startStdErrLogging();
    startStdOutLogging(env, obj);

    __android_log_write(ANDROID_LOG_INFO, __FUNCTION__, "We are leaving run Python");
    return lExecuteReturn;
}

JNIEXPORT void JNICALL Java_com_example_testedit_PythonThread_returnInfoPython
        (JNIEnv *env, jobject obj) {
    jniObj.env=env;
    jniObj.obj=obj;
    returnPython(jniObj.env,jniObj.obj, errorMessage + " " + mymessage);
    mymessage = "";
    status = false;
}
JNIEXPORT void JNICALL returnPython(JNIEnv *env, jobject obj, string message) {
    jstring jstr = env->NewStringUTF(message.c_str());

    jclass cls = env->FindClass("com/example/testedit/PythonThread");

    //jmethodID retryS = env->GetMethodID(cls, "retryStartVideo", "()V");
    // env->CallVoidMethod(obJ, retryS);
    env->ExceptionClear();
    jmethodID method = env->GetMethodID(cls, "messageMe", "(Ljava/lang/String;)V");
    env->CallVoidMethod(obj, method, jstr);

}


/**
 * We check @status, if @status = true we mast read @message
 * */
JNIEXPORT jboolean JNICALL
Java_com_example_testedit_PythonThread_getStatusPy(JNIEnv *env, jobject thiz) {
    jniObj.env=env;
    jniObj.obj=thiz;
    return status;
}
/**
 * We check  @statusResult, if @statusResult = true we change on false
 * and return true else return @statusResult.
 * @statusResult needed for to see the end of the pthread cycle
 * */

JNIEXPORT jboolean JNICALL
Java_com_example_testedit_PythonThread_getStatusResult(JNIEnv *env, jobject thiz) {
    jniObj.env=env;
    jniObj.obj=thiz;
    if (statusResult) {
        statusResult = false;
        return true;
    } else
        return statusResult;
}


JNIEXPORT jboolean JNICALL
Java_com_example_testedit_PythonThread_getStatusError(JNIEnv *env, jobject thiz) {
    jniObj.env=env;
    jniObj.obj=thiz;
    return statusError;
}

JNIEXPORT jboolean JNICALL
Java_com_example_testedit_PythonThread_getStatusErrorResult(JNIEnv *env, jobject thiz) {
    jniObj.env=env;
    jniObj.obj=thiz;
    if (statusErrorResult) {
        statusErrorResult = false;
        return true;
    } else
        return statusErrorResult;
}

JNIEXPORT jstring JNICALL
Java_com_example_testedit_PythonThread_getResult(JNIEnv *env, jobject thiz) {
    jstring jstr = env->NewStringUTF(mymessage.c_str());
    mymessage="";
    status = false;
    return jstr;
}
JNIEXPORT jstring JNICALL
Java_com_example_testedit_PythonThread_getError(JNIEnv *env, jobject thiz) {
    // TODO: implement getError()
    jstring jstr = env->NewStringUTF(errorMessage.c_str());
    errorMessage="";
    status = false;
    return jstr;
    //errorMessage
}
}

// Start up our Standard Error Thread
void startStdErrLogging() {
    // This will make our stderr buffer wake on newline
    setvbuf(stderr, nullptr, _IOLBF, 0);

    /* create the pipe and redirect stdout */
    pipe(mErrFile);
    dup2(mErrFile[1], STDERR_FILENO);

    /* spawn the logging thread */
    if (pthread_create(&mErrThread, nullptr, err_thread_func, nullptr) != 0) {
        return;
    }

    pthread_detach(mErrThread);
}

void startStdOutLogging(JNIEnv *pEnv, jobject pJobject) {

    // This will make our stderr buffer wake on newline _IOLBF instead of Nonbuffered _IONBF
    setvbuf(stdout, nullptr, _IOLBF, 0);

    /* create the pipe and redirect stdout */
    pipe(mOutFile);
    dup2(mOutFile[1], STDOUT_FILENO);
    __android_log_write(ANDROID_LOG_DEBUG, __FUNCTION__, "Start thread_func");
    /* spawn the logging thread */
    // (pthread_create(&mOutThread, nullptr, out_thread_func, new MyWrapper(pEnv,pJobject)) != 0)

    if (pthread_create(&mOutThread, nullptr, out_thread_func, nullptr) != 0) {
        return;
    }

    pthread_detach(mOutThread);

}

void *out_thread_func(void *pVoid) {
    ssize_t lReadSize;
    char lReadBuffer[2048];

    // This is what we have left to send
    string lUnProcessedBuffer;

    lReadBuffer[0] = '\0';

    size_t lPos;               // the position of our \n
    string lWriteBuffer;       // What we plan to store the stuff to write out in

    // Set this read non-blocking
    fcntl(mOutFile[0], F_SETFL,
          fcntl(mOutFile[0], F_GETFL) | O_NONBLOCK); // NOLINT(hicpp-signed-bitwise)

    // Stay running until someone sets this flag to tell us to die
#pragma clang diagnostic push
#pragma ide diagnostic ignored "EndlessLoop"
    while (true) {
        lReadSize = read(mOutFile[0], lReadBuffer, sizeof lReadBuffer - 1);
        if (lReadSize <= 0) {

            // We found nothing, wait to keep the CPU usage down
            usleep(25000); // 20ms
            continue;
        }


        // Find the position of the \n in our string
        lUnProcessedBuffer.append(lReadBuffer);

        // now we have a buffer, might be more then 1 line, keep writing until we have
        // written each line
        while ((lPos = lUnProcessedBuffer.find_first_of('\n')) != string::npos) {
            status = false;
            // We know where it is.
            lWriteBuffer = lUnProcessedBuffer.substr(0, ++lPos);
            lUnProcessedBuffer = lUnProcessedBuffer.substr(lPos);
            string result = "OUTPUT " + lWriteBuffer;

            __android_log_write(ANDROID_LOG_DEBUG, __FUNCTION__, result.c_str());
            mymessage = lWriteBuffer;
            status = true;
            usleep(2000); // 20ms
        }
        statusResult = true;

    }

#pragma clang diagnostic pop

// For the sake of this demo we have no condition to stop the logging, but if you did, put this back
//   // Close the files, we are about to terminate.  This is big, else you get broken pipe
//   close(mOutFile[0]);
//   close(mOutFile[1]);
//
//   return nullptr;
// Для цієї демонстрації ми не маємо умов зупинити журналювання, але якщо ви це зробили, поверніть це назад
// // Закрийте файли, ми збираємось завершити роботу. Це великий, інакше ви зламаєте трубу
// закрити (mOutFile[0]);
// закрити(mOutFile[1]);
//
// повернути nullptr;
}

static PyObject *
spam_system(PyObject * self, PyObject * args) {
    const char *command;
    int sts;

    if (!PyArg_ParseTuple(args, "s", &command))
        return NULL;
    sts = system(command);
    return PyLong_FromLong(sts);
}

static void *err_thread_func(void *) {
    ssize_t lReadSize;
    char lReadBuffer[2048];

    // This is what we have left to send
    string lUnProcessedBuffer;

    lReadBuffer[0] = '\0';

    size_t lPos;               // the position of our \n
    string lWriteBuffer;       // What we plan to store the stuff to write out in
    //executeFunction
    //handlePythonError
    //handlePythonError
    // checkModule

    // Set this read non-blocking
    fcntl(mErrFile[0], F_SETFL,
          fcntl(mErrFile[0], F_GETFL) | O_NONBLOCK); // NOLINT(hicpp-signed-bitwise)

    // Stay running until someone sets this flag to tell us to die
#pragma clang diagnostic push
#pragma ide diagnostic ignored "EndlessLoop"
    while (true) {
        lReadSize = read(mErrFile[0], lReadBuffer, sizeof lReadBuffer - 1);

        if (lReadSize <= 0) {
            // We found nothing, wait a bit to keep the CPU usage down
            usleep(250000); // 250ms
            continue;
        }

        // Find the position of the \n in our string
        lUnProcessedBuffer.append(lReadBuffer);

        // now we have a buffer, might be more then 1 line, keep writing until we have
        // written each line
        while ((lPos = lUnProcessedBuffer.find_first_of('\n')) != string::npos) {
            // We know where it is.
            statusError = false;
            lWriteBuffer = lUnProcessedBuffer.substr(0, ++lPos);
            lUnProcessedBuffer = lUnProcessedBuffer.substr(lPos);
            string error = "OUTPUT ERROR " + lWriteBuffer;

            __android_log_write(ANDROID_LOG_ERROR, __FUNCTION__, error.c_str());
            mymessage = lWriteBuffer;
            statusError = true;
            usleep(20000); // 20ms
        }
        statusErrorResult = true;
    }
#pragma clang diagnostic pop

// For the sake of this demo we have no condition to stop the logging, but if you did, put this back
//   // Close the files, we are about to terminate
//   close(mErrFile[0]);
//   close(mErrFile[1]);
//
//   return nullptr;
}

#pragma clang diagnostic pop



#pragma once

#include "StdAfx.h"
#include <jni.h>
#include <sstream>
#include <string>
#include <vector>
#include <thread>          // so we can get the thread id
#include <unistd.h>

#include <algorithm>  // transform

#include "Python.h"


extern const char *cDirectorySlash;
extern const char *cPythonPathSeperator;
using namespace std;

class Utilities {
public:


    inline static const string getStringFromJava(JNIEnv *env, jstring aString) {
        jboolean isCopy;
        const char *lRawChar;
        lRawChar = env->GetStringUTFChars(aString, &isCopy);

        // Now save it off in a String
        string lOutput(lRawChar);

        if (isCopy == JNI_TRUE) {
            (env)->ReleaseStringUTFChars(aString, lRawChar);
        }

        return lOutput;
    }


    static const wstring getWStringFromJava(JNIEnv *env, jstring aString);

    inline static const string convertWChar(wstring aString) {
        // NOTE:  This will only convert normal characters, if you actually send something that is not UTF-8, it's gonna give garbage
        setlocale(LC_CTYPE, "");

        const wstring ws = aString;
        const string lOutput(ws.begin(), ws.end());

        return lOutput;
    }

    static inline const bool fileExists(const string &aFileName) {
        // Make sure that the file exists before we try to call and fail
        return (access(aFileName.c_str(), F_OK) != -1);
    }

    static inline const bool dirExists(const string &aDirName) {
        if (access(aDirName.c_str(), 0) == 0) {
            struct stat status;
            stat(aDirName.c_str(), &status);

            return (status.st_mode & S_IFDIR) != 0;
        }
        return false;
    }

    //////////////////////////////////////////////////////////////////
    /** \brief Take a string separated by aDelimiter(') and separated it into a vector of strings
    *  and trim the values
     *  \param  const string& aStringToSplit - The string to split
     *  \param const string& aDelimiter - The delimiter to split at
     *  \param vector<string>& aVectorOfStrings - The vector of returned strings
     *  \return void
     *  \example
     *  With aDelimiter = "|"
     *          aStringToSplit ""      = aVectorOfStrings[0] = ""
     *          aStringToSplit "|"     = aVectorOfStrings[0] = ""  and aVectorOfStrings[1] = ""
     *          aStringToSplit "a|a"   = aVectorOfStrings[0] = "a" and aVectorOfStrings[1] = "a"
     *          aStringToSplit "a|"    = aVectorOfStrings[0] = "a" and aVectorOfStrings[1] = ""
     */
    inline static void split_trim(const string &aStringToSplit,
                                  const string &aDelimiter,
                                  vector<string> &aWords) {
        size_t lBeginPos;
        size_t lEndPos;
        string lItem;
        for (lBeginPos = 0, lEndPos = aStringToSplit.find(aDelimiter);
             lEndPos != string::npos;
             lEndPos = aStringToSplit.find(aDelimiter, lEndPos)) {
            lItem = aStringToSplit.substr(lBeginPos, lEndPos - lBeginPos);
            trim(lItem);

            aWords.push_back(lItem);
            lEndPos += aDelimiter.length();
            lBeginPos = lEndPos;
        }

        lItem = aStringToSplit.substr(lBeginPos);
        trim(lItem);
        aWords.push_back(lItem);  // the last word..
    }

    inline static void trim(string &aStringToTrim, const char *aTrimString = " \t") {
        size_t lRLocation = aStringToTrim.find_first_not_of(aTrimString);
        size_t lLLocation = aStringToTrim.find_last_not_of(aTrimString);
        if (lRLocation != string::npos && lLLocation != string::npos) {
            aStringToTrim = aStringToTrim.substr(lRLocation, lLLocation - lRLocation + 1);
            return;
        }
        if (lRLocation != string::npos && lLLocation == string::npos) {
            aStringToTrim = aStringToTrim.substr(lRLocation, aStringToTrim.size());
            return;
        }

        aStringToTrim.clear();
    };
};

class NoCaseFunctionObject {
public:
    //constructor:  initialized the comparison criterion
    NoCaseFunctionObject() {};

    inline bool operator()(const string &aS1, const string &aS2) const {
        return lexicographical_compare(aS1.begin(), aS1.end(),
                                       aS2.begin(), aS2.end(),
                                       this->nocase_compare);
    }

private:
    inline static bool nocase_compare(char c1, char c2) {
        // Since this is used for a map. The comparison has to be a less than operator rather than an equality operation.
        return toupper(c1) < toupper(c2);
    }
};
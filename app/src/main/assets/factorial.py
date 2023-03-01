#Python program to find the factorial of a number provided by the user.
#import time
print('FACTORIAL')
def main():
    import time
    # create parrot1 object
    # change the value for a different result
    num = 15
    # To take input from the user
    #num = int(input("Enter a number: "))
    factorial = 1
    # check if the number is negative, positive or zero
    if num < 0:
        print("Sorry, factorial does not exist for negative numbers")
    elif num == 0:
        print("The factorial of 0 is 1")
    else:
        for i in range(1,num + 1):
            factorial = factorial*i
            #time.sleep(0.5)
                #time.sleep(6)
            print(factorial)

        print("The factorial of",num,"is",factorial)


    parrons()

if __name__ == '__main__':
    main()

def parrons():
    print('PATRON')

    # create parrot1 object
    parrot1 = Parrot()
    parrot1.name = "Blu"
    parrot1.age = 10

    # create another object parrot2
    parrot2 = Parrot()
    parrot2.name = "Woo"
    parrot2.age = 15

    # access attributes handlePythonError checkModule

    print(f"{parrot1.name} is {parrot1.age} years old")
    print(f"{parrot2.name} is {parrot2.age} years old")
    ger="dsd"
class Parrot:

    # class attribute
    name = ""
    age = 0

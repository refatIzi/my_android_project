

#A Fibonacci sequence is the integer sequence of 0, 1, 1, 2, 3, 5, 8....

#The first two terms are 0 and 1. All other terms are obtained by adding the preceding two terms. This means to say the nth term is the sum of (n-1)th and (n-2)th term.

# Program to display the Fibonacci sequence up to n-th term


#status
print('++++++++++++++')

def main():
    print('WE START PYTHON FILE')
    import os
    dir_path = os.path.dirname(os.path.realpath(__file__))
    out_file = '{}/HelloWorld.txt'.format(dir_path)
    with open(out_file, 'a') as the_file:
        the_file.write('It works!\n')


    import time
    print(time.time())
    with open(out_file, 'a') as the_file:
        the_file.write('It Still works!\n')

    import sys
    print('SYS VERSION ')
    print(sys.version)
    print('SYS VERSION info ')
    print(sys.version_info)

    print('You should see this #1')
    try:
        import requests
    except:
        print('VRETETETETERFD DFSRRS You should NOT see this!!!!')
    finally:
        print('You should see this #2')
        set()



if __name__ == '__main__':
    main()


def set():
     print('FACTORIAL')
     #Python program to find the factorial of a number provided by the user.

     # change the value for a different result
     num = 9

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
        print("The factorial of",num,"is",factorial)


def Karatsuba(x, y, num_of_digits):
    if (num_of_digits == 1):
        return x * y
    else:
        digit_count = 0
        x_second_half = x_first_half = y_second_half = y_first_half = 0
        a = b = c = d = ac = ad = bc = bd = num1num2 = 0
        half_digits = num_of_digits//2
        while(digit_count != half_digits):
            x_last_digit = 0
            y_last_digit = 0
            x_last_digit = x % 10            
            y_last_digit = y % 10          
            x //= 10                    #Remove last digit of x
            y //= 10                    #Remove last digit of y
            if(x_second_half == 0):
                x_second_half += x_last_digit
                y_second_half += y_last_digit
            else:
                x_second_half += pow(10, digit_count) * x_last_digit
                y_second_half += pow(10, digit_count) * y_last_digit
            digit_count +=1
        x_first_half = x
        y_first_half = y
        a = x_first_half
        b = x_second_half
        c = y_first_half
        d = y_second_half
        ac = Karatsuba(a, c, half_digits)
        ad = Karatsuba(a, d, half_digits)
        bc = Karatsuba(b, c, half_digits)
        bd = Karatsuba(b, d, half_digits)
        xy = pow(10, num_of_digits) * ac + pow(10, half_digits) * (ad + bc) + bd
        return xy

x = 3141592653589793238462643383279502884197169399375105820974944592
y = 2718281828459045235360287471352662497757247093699959574966967627
result = 8539734222673567065463550869546574495034888535765114961879601127067743044893204848617875072216249073013374895871952806582723184
num_of_digits = 64
xy = Karatsuba(x, y, num_of_digits)
if (x*y == result):
    print("correct!")
print("x * y = ", xy)
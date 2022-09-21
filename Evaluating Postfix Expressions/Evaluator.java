import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {

    /**
     * Takes a symbolic/numeric infix expression as input and converts it to
     * postfix notation. There is no assumption on spaces between terms or the
     * length of the term (e.g., two digits symbolic or numeric term)
     *
     * @param expression infix expression
     * @return postfix expression
     */

    public String infixToPostfix(String expression);

    /**
     * Evaluate a postfix numeric expression, with a single space separator
     * 
     * @param expression postfix expression
     * @return the expression evaluated value
     */

    public int evaluate(String expression);

}

public class Evaluator implements IExpressionEvaluator {
    static int a, b, c;

    class stack {
        class Node {
            int element;
            Node next;
        }

        int length = 0;
        Node top = null;

        public void push(int element) {
            Node newNode = new Node();
            newNode.element = element;
            if (length == 0) {
                top = newNode;
                top.next = null;
            } else {
                newNode.next = top;
                top = newNode;

            }
            length++;
        }

        public int peek() {
            if (isEmpty()) {
                System.out.println("Error");
                System.exit(0);
                return 'n';
            } else {
                return top.element;
            }
        }

        public int pop() {
            if (length == 0) {
                System.out.println("Error");
                System.exit(0);
                return 'n';
            } else {
                Node cur = top;
                top = top.next;
                length--;
                return cur.element;
            }
        }

        public boolean isEmpty() {
            return length == 0;
        }
    }

    boolean isOperator(char c) {
        char[] arr = { '+', '-', '*', '/', '^', '(', ')' };
        for (char i : arr) {
            if (c == i)
                return true;
        }
        return false;
    }

    int rank(char c) {
        if (c == '+' || c == '-')
            return 1;
        else if (c == '*' || c == '/')
            return 2;
        else if (c == '^')
            return 3;
        else if (c == '(')
            return 4;
        else
            return 0;
    }

    boolean check(char c, char ch) {
        return rank(c) > rank(ch);
    }

    @Override
    public String infixToPostfix(String expression) {
        try {
            String str = "";
            stack stack = new stack(), stack2 = new stack();
            boolean he = false;
            for (int i = 0; i < expression.length(); i++) {
                if (isOperator(expression.charAt(i))) {
                    if (expression.charAt(i) == '-' && expression.charAt(i + 1) == '-') {
                        if ((i != 0 && stack.isEmpty())
                                || (str.length() != 0 && stack.isEmpty() && !isOperator(str.charAt(str.length() - 1))))
                            expression = expression.substring(0, i + 1) + '+' + expression.substring(i + 2);
                        else {
                            expression = expression.substring(0, i + 1) + expression.substring(i + 2);
                        }
                        continue;
                    }
                    if (expression.charAt(i) == '(')
                        stack2.push(expression.charAt(i));

                    if (stack.isEmpty()) {
                        stack.push(expression.charAt(i));
                    } else if ((check((char) expression.charAt(i),
                            (char) stack.peek()) && expression.charAt(i) != ')')
                            || stack.peek() == '(') {
                        if (stack.peek() == '(')
                            he = true;
                        stack.push(expression.charAt(i));
                    } else if (expression.charAt(i) == ')') {
                        if (stack2.peek() != '(') {
                            System.out.println("Error");
                            System.exit(0);
                        }
                        stack2.pop();
                        he = false;
                        while (stack.peek() != '(') {
                            str += (char) stack.pop();
                        }
                        stack.pop();
                    } else if (!check(expression.charAt(i), (char) stack.peek())) {
                        str += (char) stack.pop();
                        stack.push(expression.charAt(i));
                    }
                } else if (isOperand(expression.charAt(i))) {
                    str += (char) expression.charAt(i);
                } else {
                    System.out.println("Error");
                    System.exit(0);
                }
            }
            if (he) {
                System.out.println("Error");
                System.exit(0);
            }
            while (!stack.isEmpty())
                str += (char) stack.pop();
            return str;
        } catch (Exception e) {
            System.out.println("Error");
            System.exit(0);
        }
        return "";
    }

    boolean isOperand(char c) {
        if (c == 'a' || c == 'b' || c == 'c')
            return true;
        return false;
    }

    int doOberation(int num1, int num2, char ch) {
        int result = 0;
        if (ch == '+') {
            result = num1 + num2;
        } else if (ch == '-')
            result = num1 - num2;
        else if (ch == '*') {
            if (num1 == 0) {
                System.out.println("Error");
                System.exit(0);
            }
            result = num1 * num2;
        } else if (ch == '/') {
            if (num1 == 0) {
                System.out.println("Error");
                System.exit(0);
            }
            result = num1 / num2;
        } else if (ch == '^') {
            if (num1 == 0) {
                System.out.println("Error");
                System.exit(0);
            }
            result = (int) Math.pow(num1, num2);
        } else {
            System.out.println("Error");
            System.exit(0);
        }
        return result;
    }

    @Override
    public int evaluate(String expression) {
        try {
            stack stack = new stack();
            for (int i = 0; i < expression.length(); i++) {
                if (isOperator(expression.charAt(i))) {
                    int num1 = stack.pop();
                    int num2 = 0;
                    if (!stack.isEmpty())
                        num2 = stack.pop();
                    stack.push(doOberation(num2, num1, expression.charAt(i)));
                } else {
                    stack.push(getResult(expression.charAt(i)));
                }
            }
            return (int) stack.pop();

        } catch (Exception e) {
            System.out.println("Error");
            System.exit(0);
        }
        return 0;
    }

    int getResult(char ch) {
        if (ch == 'a')
            return a;
        else if (ch == 'b')
            return b;
        else if (ch == 'c')
            return c;
        else {
            System.out.println("Error");
            System.exit(0);
            return 0;
        }
    }

    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            String str = sc.nextLine();
            str.replaceAll("^--", "^");
            Evaluator object = new Evaluator();
            str = object.infixToPostfix(str);
            String st1 = sc.nextLine();
            String[] s = st1.split("=");
            if (!s[0].equals("a")) {
                System.out.println("Error");
                System.exit(0);
            }
            a = Integer.parseInt(s[1]);
            String st2 = sc.nextLine();
            String[] s2 = st2.split("=");
            if (!s2[0].equals("b")) {
                System.out.println("Error");
                System.exit(0);
            }
            b = Integer.parseInt(s2[1]);
            String st3 = sc.nextLine();
            String[] s3 = st3.split("=");
            if (!s3[0].equals("c")) {
                System.out.println("Error");
                System.exit(0);
            }
            c = Integer.parseInt(s3[1]);
            int num = object.evaluate(str);
            System.out.println(str);
            System.out.println(num);
        } catch (Exception e) {
            System.out.println("Error");
            System.exit(0);

        }
    }
}
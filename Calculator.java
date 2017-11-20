package edu.tuskegee.cs499h.simplecalculator;

import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Calculator extends Activity
{
   private double operand1, operand2, result;
   private String input = "";
   private boolean operator_used, period_used;
   private TextView output, debug;
   private Stack<Double> operands;
   private Stack<String> operators;

   @Override
   public void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_calculator);
      output = (TextView) findViewById(R.id.display);
      debug = (TextView) findViewById(R.id.debug);
      debug.setText("<Calculate!>");
      operand1 = 0.0;
      operand2 = 0.0;
      operator_used = false;
      period_used = false;
      operands = new Stack<Double>();
      operators = new Stack<String>();
   }

   public void digitClicked(View view)
   {
      debug.setText("<Calculate!>");                                             // display debug message
      
      Button button = (Button) (view);                                           // get Button copy of view

      if (operands.size() == 0)                                                  // if no operand, then new digit becomes operand 1
      {
         input = (String) button.getText();                                      // get digit
         operands.push(Double.parseDouble(input));                               // push digit into stack

         //debug.setText(input);                                                   // debug
      } 
      else if (operands.size() == 1)                                             // if 1 operand, then new digit amended to operand 1 
                                                                                 // or new digit becomes operand 2
      {
         if (operator_used)                                                      // if 1 operator, then new digit becomes operand 2
         {
            input = (String) button.getText();                                   // get digit
            operands.push(Double.parseDouble(input));                            // push digit into stack

            //debug.setText(input);                                                // debug
         }
         else                                                                    // if no operator, then new digit amended to operand 1
         {
            operands.pop();                                                      // remove operand from stack
            input = input + button.getText();                                    // amend digit label to end of operand
                                                                                                         
            operands.push(Double.parseDouble(input));                            // push operand into stack

            //debug.setText(input);
         }
      }
      else if (operands.size() == 2)                                             // 2 operands (1 operator)
      {
         operands.pop();                                                         // remove operand from stack
         input = input + button.getText();                                       // amend digit label to end of operand
                                           
         operands.push(Double.parseDouble(input));                               // push operand into stack

         //debug.setText(input);                                                   // debug
      }

      output.setText(input);                                                     // display digit
   }
   
   /** Operator Clicked */
   public void operatorClicked(View view)
   {
      debug.setText("<Calculate!>");                                             // display debug message

      if (operands.size() == 2)                                                  // if 2 operands (complete expression), then 
                                                                                 // evaluate expression
      {
         operand2 = operands.pop();                                              // pop operand from operand stack
         operand1 = operands.pop();                                              // pop operand from operand stack

         result = calculate(operand1, operand2, operators.pop());                // evaluate expression
         operator_used = false;                                                  // no operator used
         operands.push(result);                                                  // push result into operand stack
         input = String.valueOf(result);                                         // set input
         if (!debug.getText().equals("undefined"))                               // sub-case: did not divide by zero
         {
            output.setText(input);                                               // display result
            //debug.setText(input);                                                // display debug info
         }
      }
      else
      {
         Button button = (Button) (view);                                           // get operator info
         if (!button.getText().equals("=") && !operator_used)                       // if "=" was not pushed and no operator
         {
            operators.push((String) button.getText());                              // push operator into operator stack
            operator_used = true;                                                   // operator used
         }
         
         input = "";                                                                // clear input
      }      
   }
   
   /** Period Clicked */
   public void periodClicked(View view)
   {
      debug.setText("<Calculate!>");                                             // display debug message

      Integer operand;
      
      if (operands.empty())                                                      // if no operands, then push .0
      {
         input = "0.";                                                           // 0. becomes operand 1
         operands.push(Double.parseDouble(input));                               // push the operand into stack
         period_used = true;
      }
      else if (operands.size() == 1)                                             // if 1 operand, then '.' amended to operand 1 or
                                                                                 // 0. becomes operand 2
      {
         if (!operator_used && !period_used)                                     // if operator not used and period not used for operand 1,
                                                                                 // then amend '.' to operand 1
         {
            operand = operands.pop().intValue();                                 // pop out integer version of operand
            input = operand.toString();

            input = input + ".";                                                 // amend decimal to end of operand 1
            operands.push(Double.parseDouble(input));                            // push the amended operand 1 into stack
            period_used = true;                                                  // period used            
         }
         else if (operator_used)                                                 // if operator used
         {
            input = "0.";                                                        // 0. becomes operand 2
            operands.push(Double.parseDouble(input));                            // push the operand into stack
            //period_used = true;                                                  // period used            
         }
      }
      else if (operands.size() == 2)                                             // if 2 operands, then amend '.' to operand 2
      {
         operand = operands.pop().intValue();                                    // pop out integer version of operand
         input = operand.toString();

         input = input + ".";                                                    // amend decimal to end of operand
         operands.push(Double.parseDouble(input));                               // push the amended operand into stack
      }

      /*if ((operands.empty() && !operator_used) || (operands.size() == 1 && operator_used)) // no operand,no operator || 1 operand,1 operator
      {
         input = "0.";
         operands.push(Double.parseDouble(input));                               // push the operand into stack
      } 
      else
      {
         operand = operands.pop().intValue();                                    // pop out integer version of operand
         input = operand.toString();

         input = input + ".";                                                    // amend decimal to end of operand
         operands.push(Double.parseDouble(input));                               // push the amended operand into stack
      }*/

      output.setText(input);                                                     // display digit
   }

   public void resetDevice()
   {
      input = "";
      operand1 = 0.0;
      operand2 = 0.0;
      output.setText("0");
      operator_used = false;
      period_used = false;
      debug.setText("<Calculate!>");
      operands.removeAllElements();
      operators.removeAllElements();
   }

   public void clearClicked(View view)
   {
      resetDevice();
   }

   

   @Override
   public void onStop()
   {
      super.onStop();
      resetDevice();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu)
   {
      getMenuInflater().inflate(R.menu.activity_calculator, menu);
      return true;
   }

   private double calculate(double operand1, double operand2, String operator)
   {
      if (operator.equals("+"))                                                  // case: addition
      {
         return operand1 + operand2;                                             // return sum
      } 
      else if (operator.equals("-"))                                             // case: subtraction
      {
         return operand1 - operand2;                                             // return difference
      }
      else if (operator.equals("*"))                                             // case: multiplication
      {
         return operand1 * operand2;                                             // return product
      } 
      else                                                                       // case: division
      {
         if (operand2 != 0)                                                      // sub-case: not divide by zero
         {
            return operand1 / operand2;                                          // return quotient
         } 
         else                                                                    // case: divide by zero
         {
            resetDevice();                                                       // reset device
            debug.setText("<undefined>");                                        // display debug message
            input = "";                                                          // clear input
            return 0.0;                                                          // return 0.0
         }
      }
   }
}

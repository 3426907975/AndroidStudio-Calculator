package com.icat.calculator;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

public class Function {

    /*
        关于进制
    */
    public static class system {
        /*
            删除二进制文本前所有的零
        */
        public static String DelBiZero(String B) {
            String res = B;
            int idx = 0;
            while (res.charAt(idx) == '0' && idx < res.length() - 1)
                idx++;
            if (idx == 0) return res;
            res = res.subSequence(idx, res.length()).toString();
            return res;
        }

        /*
            格式化二进制文本
        */
        public static String DeBI(String B) {
            String res = B.replace(" ", "");
            if ("".equals(res.replace("0", ""))) return "0000";

            res = DelBiZero(res);

            while (res.length() % 4 != 0)
                res = "0" + res;

            StringBuffer resB = new StringBuffer(res);
            int len = resB.length();

            for (int i = 0; i < len - 4; i++) {
                if ((i + 1) % 4 == 0) {
                    resB.insert(i + 1 + (resB.length() - len), " ");
                }
            }

            return resB.toString();
        }

        /*
            十进制转换为 n 进制
        */
        public static String DETo(int n, int num) {
            // n 表示目标进制, num 要转换的值
            String str = "";
            int yushu;      // 保存余数
            int shang = num;      // 保存商
            while (shang > 0) {
                yushu = shang % n;
                shang = shang / n;

                // 10-15 -> a-f
                if (yushu > 9)
                    str = (char) ('A' + (yushu - 10)) + str;
                else
                    str = yushu + str;

            }
            return str;
        }

        /*
            任意进制数转为十进制数
        */
        public static String toDe(String a, int b) {
            int r = 0;
            for (int i = 0; i < a.length(); i++) {

                r = (int) (r + formatting(a.substring(i, i + 1))
                        * Math.pow(b, a.length() - i - 1));
            }
            return String.valueOf(r);
        }

        /*
            将十六进制中的字母转为对应的数字
        */
        public static int formatting(String a) {
            int i = 0;
            for (int u = 0; u < 10; u++) {
                if (a.equals(String.valueOf(u)))
                    i = u;
            }
            if (a.equals("A"))
                i = 10;
            if (a.equals("B"))
                i = 11;
            if (a.equals("C"))
                i = 12;
            if (a.equals("D"))
                i = 13;
            if (a.equals("E"))
                i = 14;
            if (a.equals("F"))
                i = 15;
            return i;
        }

        /*
            将十进制中的数字转为十六进制对应的字母
        */
        public static String formattingH(int a) {
            String i = String.valueOf(a);
            switch (a) {
                case 10:
                    i = "A";
                    break;
                case 11:
                    i = "B";
                    break;
                case 12:
                    i = "C";
                    break;
                case 13:
                    i = "D";
                    break;
                case 14:
                    i = "E";
                    break;
                case 15:
                    i = "F";
                    break;
            }
            return i;
        }
    }

    /*
        关于JavaScript
    */
    public static class JavaScript {
        /*
            执行JavaScript代码
        */
        public static String runScript(Object c, String js, String functionName, Object[] functionParams) {
            Context rhino = Context.enter();
            rhino.setOptimizationLevel(-1);
            try {
                Scriptable scope = rhino.initStandardObjects();

                ScriptableObject.putProperty(scope, "javaContext", Context.javaToJS(c, scope));
                ScriptableObject.putProperty(scope, "javaLoader", Context.javaToJS(MainActivity.class.getClassLoader(), scope));

                rhino.evaluateString(scope, js, "MainActivity", 1, null);

                org.mozilla.javascript.Function function = (org.mozilla.javascript.Function) scope.get(functionName, scope);

                Object result = function.call(rhino, scope, scope, functionParams);
                if (result instanceof String) {
                    return (String) result;
                } else if (result instanceof NativeJavaObject) {
                    return (String) ((NativeJavaObject) result).getDefaultValue(String.class);
                } else if (result instanceof NativeJavaObject) {
                    return (String) ((NativeObject) result).getDefaultValue(String.class);
                }
                return result.toString();//(String) function.call(rhino, scope, scope, functionParams);
            } catch (Exception e) {

            } finally {
                Context.exit();
            }
            return null;
        }

        /*
            执行JavaScriptEval
        */
        public static String JavascriptEval(Object c, String js) {
            String jsCode = "function res(){return eval(" + "\"" + js + "\"" + ");}";
            //jsCode = "function res() { return 10; }";
            jsCode = jsCode.replace("×", "*");
            jsCode = jsCode.replace("÷", "/");
            return runScript(c, jsCode.replace("\\r\\n", ""), "res", new String[]{});
        }
    }
}

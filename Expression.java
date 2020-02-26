package app;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import structures.Stack;

public class Expression {

		public static String delims = " \t*+-/()[]";

		private static boolean arrayExists(ArrayList<Array> arrays, String name) {
		for(int j = 0; j < arrays.size(); j++) {
		if(arrays.get(j).name.equals(name)) {
		return true;
		}
		}
		return false;

		}
		private static boolean variableExists(ArrayList<Variable> vars, String name) {
		for(int j = 0; j < vars.size(); j++) {
		if(vars.get(j).name.equals(name)) {
		return true;
		}
		}
		return false;
		}
		public static void makeVariableLists(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		String current_name;
		expr = expr.trim();
		char exprTokens [] = expr.toCharArray();
		loop:
		for(int i = 0; i < expr.length(); i ++) {
		char current = exprTokens[i];
		if(Character.isLetter(current)) {

		switch(current) {
		case 'v':
		case 'V':
		if(exprTokens.length > i+2) {
		if(exprTokens[i+1] == 'a' && exprTokens[i+2] == 'r') {
		int end = i;
		while(Character.isLetter(exprTokens[end]) && end < exprTokens.length-1) {
		end++;
		}
		if(end == exprTokens.length-1 && Character.isLetter(exprTokens[end])) {
		current_name = expr.substring(i, end+1);
		} else {
		current_name = expr.substring(i, end);
		}
		if(exprTokens[end] == '[') {
		if(!arrayExists(arrays, current_name)) {
		arrays.add(new Array(current_name));


		}
		i = end;
		continue loop;
		}
		if(!variableExists(vars, current_name)) {
		vars.add(new Variable(current_name));


		}
		i = end;
		continue loop;
		}
		}
		int end = i;
		while(Character.isLetter(exprTokens[end]) && end < exprTokens.length-1) {
		end++;
		}
		if(end == exprTokens.length-1 && Character.isLetter(exprTokens[end])) {
		current_name = expr.substring(i, end+1);
		} else {
		current_name = expr.substring(i, end);
		}
		if(exprTokens[end] == '[') {
		if(!arrayExists(arrays, current_name)) {
		arrays.add(new Array(current_name));

		}
		i = end;
		continue loop;
		}
		if(!variableExists(vars, current_name)) {
		vars.add(new Variable(current_name));
		}
		i = end;
		continue loop;

		default:
		end = i;
		while(Character.isLetter(exprTokens[end]) && end < exprTokens.length-1) {
		end++;
		}
		if(end == exprTokens.length-1 && Character.isLetter(exprTokens[end])) {
		current_name = expr.substring(i, end+1);
		} else {
		current_name = expr.substring(i, end);
		}
		if(exprTokens[end] == '[') {
		if(!arrayExists(arrays, current_name)) {
		arrays.add(new Array(current_name));
		}
		i = end;
		continue loop;
		}
		if(!variableExists(vars, current_name)) {
		vars.add(new Variable(current_name));
		}
		i = end;
		continue loop;


		}
		}
		}

		}


		public static void
		loadVariableValues(Scanner sc, ArrayList<Variable> vars, ArrayList<Array> arrays)
		throws IOException {
		while (sc.hasNextLine()) {
		StringTokenizer st = new StringTokenizer(sc.nextLine().trim());
		int numTokens = st.countTokens();
		String tok = st.nextToken();
		Variable var = new Variable(tok);
		Array arr = new Array(tok);
		int vari = vars.indexOf(var);
		int arri = arrays.indexOf(arr);
		if (vari == -1 && arri == -1) {
		continue;
		}
		int num = Integer.parseInt(st.nextToken());
		if (numTokens == 2) {
		vars.get(vari).value = num;
		} else {
		arr = arrays.get(arri);
		arr.values = new int[num];
		while (st.hasMoreTokens()) {
		tok = st.nextToken();
		StringTokenizer stt = new StringTokenizer(tok," (,)");
		int index = Integer.parseInt(stt.nextToken());
		int val = Integer.parseInt(stt.nextToken());
		arr.values[index] = val;
		}
		}
		}
		}

		private static int getVarVal(ArrayList<Variable> vars, String name) {
		int i = 0;
		while(!vars.get(i).name.equals(name)) {
		i++;
		}

		return vars.get(i).value;
		}

		private static int getArrVal(ArrayList<Array> arrays, String name, int index) {
		int i = 0;
		while(!arrays.get(i).name.equals(name)) {
		i++;
		}
		return arrays.get(i).values[index];
		}
		private static boolean isPrecedent(char currOp, char nextOp) {
		if((currOp == '*' || currOp == '/') && (nextOp == '-' || nextOp == '+')) {
		return false;
		}
		if((nextOp ==')' || nextOp == '(')){
		return false;
		}
		return true;
		}
		private static float operate(float b, float a, char currOp) {
		float value = 0;
		switch(currOp) {
		case '*':
		value = (a*b);
		break;
		case '/':
		value = (a/b);
		break;
		case '+':
		value = (a+b);
		break;
		case '-':
		value = (a-b);
		}
		return value;
		}

		public static float evaluate(String expr, ArrayList<Variable> vars, ArrayList<Array> arrays) {
		if(expr.isEmpty()) {
		return 0;
		}
		Stack<Float> numStack = new Stack<Float>();
		Stack<Character> opStack = new Stack<Character>();
		String current_name;
		int end = 0;
		expr = expr.trim();
		String [] exprTokens = expr.split("");

		loop:
		for(int i = 0; i < exprTokens.length; i++) {

		char current = exprTokens[i].charAt(0);
		if(Character.isDigit(current)){
		end = i;
		while(Character.isDigit(exprTokens[end].charAt(0)) && end < exprTokens.length-1) {
		end++;
		}
		if(end == exprTokens.length-1 && Character.isDigit(exprTokens[end].charAt(0))) {
		end = end+1;
		}
		numStack.push((float) Long.parseLong(expr.substring(i, end)));
		i = end-1;
		continue loop;


		} else if(Character.isLetter(current)) {
		end = i;
		while(Character.isLetter(exprTokens[end].charAt(0)) && end < exprTokens.length-1) {
		end++;
		}
		if(end == exprTokens.length-1 && Character.isLetter(exprTokens[end].charAt(0))) {
		current_name = expr.substring(i, end+1);

		} else {
		current_name = expr.substring(i, end);

		}
		if(variableExists(vars, current_name)) {
		numStack.push((float) getVarVal(vars, current_name));

		if(end == exprTokens.length-1) {
		i = end;

		} else {
		i = end-1;
		}
		continue loop;
		}

		i = end+1;
		end = i;
		int nests = 1;
		int endNests = 0;
		while(nests > endNests) {
		if(exprTokens[end].charAt(0) == '[') {
		nests++;
		} else if(exprTokens[end].charAt(0)== ']') {
		endNests++;
		if(endNests == nests) {
		break;
		}
		}
		end++;
		}

		float index = evaluate(expr.substring(i, end),vars, arrays);
		numStack.push((float) getArrVal(arrays, current_name, (int) index));
		i = end;
		continue loop;
		} else {
		switch(current) {
		case '(':

		i = i+1;

		end = i;

		int nests = 1;

		int endNests = 0;

		while(nests > endNests) {
		if(exprTokens[end].charAt(0) == '(') {
		nests++;
		} else if(exprTokens[end].charAt(0) == ')') {
		endNests++;
		if(endNests == nests) {
		break;
		}
		}
		end++;
		}
		numStack.push(evaluate(expr.substring(i, end), vars, arrays));
		i = end;
		continue loop;
		case ' ':
		continue loop;
		default:
		while(!opStack.isEmpty() && isPrecedent(current, opStack.peek())) {
		numStack.push(operate(numStack.pop(), numStack.pop(), opStack.pop()));
		}
		opStack.push(current);

		}
		}
		}
		while(!opStack.isEmpty()) {
		numStack.push(operate(numStack.pop(), numStack.pop(), opStack.pop()));
		}
		return numStack.pop();

		}
	}


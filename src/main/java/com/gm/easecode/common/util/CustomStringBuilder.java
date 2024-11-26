package com.gm.easecode.common.util;

public class CustomStringBuilder{
	public static final String TAB = "\t";
	public static final String BREAK_ROW_CHAR = "\n";
	private StringBuilder _sb = null;
	public CustomStringBuilder(){
		_sb = new StringBuilder();
	}
	public CustomStringBuilder(String str){
		_sb = new StringBuilder(str);
	}
	public CustomStringBuilder append(boolean b){
		_sb.append(b);
		return this;
	}
	public CustomStringBuilder append(char c){
		_sb.append(c);
		return this;
	}
	public CustomStringBuilder append(char[] str){
		_sb.append(str);
		return this;
	}
	public CustomStringBuilder append(char[] str,int offset,int len){
		_sb.append(str,offset,len);
		return this;
	}
	public CustomStringBuilder append(CharSequence s){
		_sb.append(s);
		return this;
	}
	public CustomStringBuilder append(CharSequence s,int start,int end){
		_sb.append(s,start,end);
		return this;
	}
	public CustomStringBuilder append(double d){
		_sb.append(d);
		return this;
	}
	public CustomStringBuilder append(float f){
		_sb.append(f);
		return this;
	}
	public CustomStringBuilder append(int i){
		_sb.append(i);
		return this;
	}
	public CustomStringBuilder append(long lng){
		_sb.append(lng);
		return this;
	}
	public CustomStringBuilder append(Object obj){
		_sb.append(obj);
		return this;
	}
	public CustomStringBuilder append(String str){
		_sb.append(str);
		return this;
	}
	public CustomStringBuilder append(StringBuffer sb){
		_sb.append(sb);
		return this;
	}
	public CustomStringBuilder appendCodePoint(int codePoint){
		_sb.appendCodePoint(codePoint);
		return this;
	}
	public int capacity(){
		return _sb.capacity();	
	}
	public char charAt(int index){
		return _sb.charAt(index);
	}
	public int codePointAt(int index){
		return _sb.codePointAt(index);
	}
	public int codePointBefore(int index){
		return _sb.codePointBefore(index);
	}
	public int codePointCount(int beginIndex,int endIndex){
		return _sb.codePointCount(beginIndex, endIndex);
	}
	public CustomStringBuilder delete(int start,int end){
		_sb.delete(start, end);
		return this;
	}
	public CustomStringBuilder deleteCharAt(int index){
		_sb.deleteCharAt(index);
		return this;
	}
	public void ensureCapacity(int minimumCapacity){
		_sb.ensureCapacity(minimumCapacity);
	}
	public void getChars(int srcBegin,int srcEnd,char[] dst,int dstBegin){
		_sb.getChars(srcBegin, srcEnd, dst, dstBegin);
	}
	public int indexOf(String str){
		return _sb.indexOf(str);
	}
	public int indexOf(String str,int fromIndex){
		return _sb.indexOf(str, fromIndex);
	}
	public CustomStringBuilder insert(int offset,boolean b){
		_sb.insert(offset, b);
		return this;
	}
	public CustomStringBuilder insert(int offset,char c){
		_sb.insert(offset, c);
		return this;
	}
	public CustomStringBuilder insert(int offset,char[] str){
		_sb.insert(offset, str);
		return this;
	}
	public CustomStringBuilder insert(int index,char[] str,int offset,int len){
		_sb.insert(index,str,offset,len);
		return this;
	}
	public CustomStringBuilder insert(int dstOffset,CharSequence s){
		_sb.insert(dstOffset, s);
		return this;
	}
	public CustomStringBuilder insert(int dstOffset,CharSequence s,int start,int end){
		_sb.insert(dstOffset, s,start,end);
		return this;
	}
	public CustomStringBuilder insert(int offset,double d){
		_sb.insert(offset, d);
		return this;
	}
	public CustomStringBuilder insert(int offset,float f){
		_sb.insert(offset, f);
		return this;
	}
	public CustomStringBuilder insert(int offset,int i){
		_sb.insert(offset, i);
		return this;
	}
	public CustomStringBuilder insert(int offset,long l){
		_sb.insert(offset, l);
		return this;
	}
	public CustomStringBuilder insert(int offset,Object obj){
		_sb.insert(offset, obj);
		return this;
	}
	public CustomStringBuilder insert(int offset,String str){
		_sb.insert(offset, str);
		return this;
	}
	public int lastIndexOf(String str){
		return _sb.lastIndexOf(str);
	}
	public int lastIndexOf(String str,int fromIndex){
		return _sb.lastIndexOf(str,fromIndex);
	}
	public int length(){
		return _sb.length();
	}
	public int offSetByCodePoints(int index,int codePointOffset){
		return _sb.offsetByCodePoints(index, codePointOffset);
	}
	public CustomStringBuilder replace(int start,int end,String str){
		_sb.replace(start, end, str);
		return this;
	}
	public CustomStringBuilder reverse(){
		_sb.reverse();
		return this;
	}
	public void setCharAt(int index,char ch){
		_sb.setCharAt(index, ch);
	}
	public void setLength(int newLength){
		_sb.setLength(newLength);
	}
	public CharSequence subSequence(int start,int end){
		return _sb.subSequence(start, end);
	}
	public String subString(int start){
		return _sb.substring(start);
	}
	public String subString(int start,int end){
		return _sb.substring(start,end);
	}
	public String toString(){
		return _sb.toString();
	}
	public void trimToSize(){
		_sb.trimToSize();
	}
	public CustomStringBuilder newLine(){
		return newLine(1);
	}
	public CustomStringBuilder newLine(int num){
		return append(BREAK_ROW_CHAR,num);
	}
	public CustomStringBuilder appendTab(){
		return appendTab(1);
	}
	public CustomStringBuilder appendTab(int num){
		return append(TAB,num);
	}
	/**
	 * 整体添加Tab
	 * @param num
	 * @return
	 */
	public CustomStringBuilder prependTabForAll(int num){
	    StringBuilder tabBuild = new StringBuilder();
	    for(int i = 0; i < num; ++i){
	        tabBuild.append(TAB);
        }
	    int fromIndex = 0;
        while(fromIndex != -1) {
            fromIndex = _sb.indexOf(BREAK_ROW_CHAR, fromIndex);
            if (fromIndex != -1) {
                _sb.insert(++fromIndex, tabBuild);
                fromIndex += tabBuild.length();
            }
        }
        return this;
	}
	private CustomStringBuilder append(String str,int loop){
		for(int i = 0; i < loop; ++i){
			_sb.append(str);	
		}
		return this;
	}
	/**
	 * 格式化，去开始的\t与结束的\n
	 */
	public String toFormatString()
	{
		String ret = _sb.toString();
//		if(ret.startsWith(TAB))
//		{
//			ret = ret.substring(TAB.length());
//		}
		if(ret.endsWith(BREAK_ROW_CHAR))
		{
			ret = ret.substring(0, ret.length() - BREAK_ROW_CHAR.length());
		}
		ret = ret.replaceFirst("^[\t]*(.*)", "$1");
		return ret;
	}
}

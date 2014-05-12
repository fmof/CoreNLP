package edu.stanford.nlp.patterns.surface;

import java.io.Serializable;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.StringUtils;

/**
 * To present a surface pattern in more detail. The class is not completely kosher. See {@link PatternToken} for more info.
 * 
 * Author: Sonal Gupta (sonalg@stanford.edu)
 */

public class SurfacePattern implements Serializable {

  private static final long serialVersionUID = 1L;

  protected String[] prevContext;
  protected String[] nextContext;
  String prevContextStr = "", nextContextStr = "";
  protected PatternToken token;
  protected String[] originalPrev;
  protected String[] originalNext;
  protected String originalPrevStr = "";
  protected String originalNextStr = "";

  public static boolean insertModifierWildcard = false;

  public SurfacePattern(String[] prevContext, PatternToken token,
      String[] nextContext, String[] originalPrev, String[] originalNext) {
    this.setPrevContext(prevContext);
    this.setNextContext(nextContext);

    if (prevContext != null)
      prevContextStr = StringUtils.join(prevContext, " ");

    if (nextContext != null)
      nextContextStr = StringUtils.join(nextContext, " ");

    this.setToken(token);
    this.setOriginalPrev(originalPrev);
    this.setOriginalNext(originalNext);
    if (originalPrev != null)
      originalPrevStr = StringUtils.join(originalPrev, " ");
    if (originalNext != null)
      originalNextStr = StringUtils.join(originalNext, " ");
  }

  public static String getContextStr(CoreLabel tokenj,
      boolean useLemmaContextTokens, boolean lowerCaseContext) {
    String str = "";

    if (useLemmaContextTokens) {
      String tok = tokenj.lemma();
      if (lowerCaseContext)
        tok = tok.toLowerCase();
      str = "[{lemma:/\\Q" + tok.replaceAll("/", "\\\\/") + "\\E/}] ";
    } else {
      String tok = tokenj.word();
      if (lowerCaseContext)
        tok = tok.toLowerCase();
      str = "[{word:/\\Q" + tok.replaceAll("/", "\\\\/") + "\\E/}] ";

    }
    return str;
  }

  public static String getContextStr(String w) {
    String str = "[/\\Q" + w.replaceAll("/", "\\\\/") + "\\E/] ";
    return str;
  }

  public String toString(List<String> notAllowedClasses) {
    return (prevContextStr + " " + getToken().getTokenStr(notAllowedClasses) + " " + nextContextStr)
        .trim();
  }

  public String toString(String morePreviousPattern, String moreNextPattern, List<String> notAllowedClasses) {
    return (prevContextStr + " " + morePreviousPattern + " "
        + getToken().getTokenStr(notAllowedClasses) + " " + moreNextPattern + " " + nextContextStr)
        .trim();
  }

  // returns 0 is exactly equal, Integer.MAX_VALUE if the contexts are not same.
  // If contexts are same : it returns (objects restrictions on the token minus
  // p's restrictions on the token). So if returns negative then p has more
  // restrictions.
  public int equalContext(SurfacePattern p) {
    if (p.equals(this))
      return 0;
    if (prevContextStr.equals(p.prevContextStr)
        && nextContextStr.equals(p.nextContextStr)) {
      int this_restriction = 0, p_restriction = 0;
      if (this.getToken().useTag)
        this_restriction++;
      if (p.getToken().useTag)
        p_restriction++;
      this_restriction -= this.getToken().numWordsCompound;
      p_restriction -= this.getToken().numWordsCompound;
      return this_restriction - p_restriction;
    }
    return Integer.MAX_VALUE;
  }

  @Override
  public boolean equals(Object b) {
    if (!(b instanceof SurfacePattern))
      return false;
    SurfacePattern p = (SurfacePattern) b;
    if (toString().equals(p.toString()))
      // if (token.equals(p.token) && this.prevContext.equals(p.prevContext) &&
      // this.nextContext.equals(p.nextContext))
      return true;
    else
      return false;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
  
  @Override
  public String toString(){
    return toString(null);
  }

  public String toStringToWrite() {
    return prevContextStr + "##" + getToken().toStringToWrite() + "##"
        + nextContextStr;
  }

  public String toStringSimple() {
    return getOriginalPrevStr() + " <b>" + getToken().toStringToWrite()
        + "</b> " + getOriginalNextStr();
  }

  public String[] getPrevContext() {
    return prevContext;
  }

  public void setPrevContext(String[] prevContext) {
    this.prevContext = prevContext;
  }

  public String[] getNextContext() {
    return nextContext;
  }

  public void setNextContext(String[] nextContext) {
    this.nextContext = nextContext;
  }

  public PatternToken getToken() {
    return token;
  }

  public void setToken(PatternToken token) {
    this.token = token;
  }

  public String getOriginalPrevStr() {
    return originalPrevStr;
  }

  public void setOriginalPrevStr(String originalPrevStr) {
    this.originalPrevStr = originalPrevStr;
  }

  public String getOriginalNextStr() {
    return originalNextStr;
  }

  public void setOriginalNextStr(String originalNextStr) {
    this.originalNextStr = originalNextStr;
  }

  public String[] getOriginalPrev() {
    return originalPrev;
  }

  public void setOriginalPrev(String[] originalPrev) {
    this.originalPrev = originalPrev;
  }

  public String[] getOriginalNext() {
    return originalNext;
  }

  public void setOriginalNext(String[] originalNext) {
    this.originalNext = originalNext;
  }

  // public static SurfacePattern parse(String s) {
  // String[] t = s.split("##", -1);
  // String prev = t[0];
  // PatternToken tok = PatternToken.parse(t[1]);
  // String next = t[2];
  // return new SurfacePattern(prev, tok, next);
  // }

}

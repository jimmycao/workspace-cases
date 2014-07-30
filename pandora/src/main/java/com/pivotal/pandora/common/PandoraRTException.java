package com.pivotal.pandora.common;

public class PandoraRTException extends RuntimeException {

  private static final long serialVersionUID = -7935198375467141166L;
  
  public PandoraRTException() {
    super();
  }

  public PandoraRTException(String message) {
    super(message);
  }
  
  public PandoraRTException(String message, Throwable cause) {
    super(message, cause);
  }
  
  public PandoraRTException(Throwable cause) {
    super(cause);
  }

}

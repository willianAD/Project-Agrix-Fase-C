package com.betrybe.agrix.controllers.exceptions;

/**
 * CropNotFoundException.
 */
public class CropNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CropNotFoundException() {
    super("Plantação não encontrada!");
  }
}

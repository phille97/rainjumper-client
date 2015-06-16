/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.tis.phille97.Exceptions;

/**
 *
 * @author Philip
 */
public class ShittyParameterException extends Exception {

    /**
     * Creates a new instance of <code>ShittyParameter</code> without detail
     * message.
     */
    public ShittyParameterException() {
    }

    /**
     * Constructs an instance of <code>ShittyParameter</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public ShittyParameterException(String msg) {
        super(msg);
    }
}

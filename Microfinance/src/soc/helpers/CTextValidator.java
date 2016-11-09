/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Wellington
 */
public class CTextValidator extends KeyAdapter {
     @Override
    public void keyTyped(KeyEvent e) {
        char character = e.getKeyChar();
        if(!(Character.isDigit(character) || (character == KeyEvent.VK_DELETE) ||(character == KeyEvent.VK_BACK_SPACE))){
            java.awt.Toolkit.getDefaultToolkit().beep();
            e.consume();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
    
}

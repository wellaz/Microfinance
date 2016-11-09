/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.ent;

/**
*
* @author Wellington
*/
public class Renderer {
	
	public String getCSSFile() {
		return ((this.getClass().getResource("style1.css").toExternalForm()));
	}

}


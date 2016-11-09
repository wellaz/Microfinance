/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 *
 * @author Wellington
 */
public class IconImage {
	public Image createDownloadRefreshImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/refresh.png")).getImage();
        return img;
    }
	public Image createDownloadmage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/download.png")).getImage();
        return img;
    }
	public Image createCloseImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/close.png")).getImage();
        return img;
    }
    
    public Image createIconImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/sigma_.png")).getImage();
        return img;
    }
    public Image createLoaderImage0(){
        Image img = new ImageIcon(this.getClass().getResource("resources/final.png")).getImage();
        return img;
    }
    public Image createPDFImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/pdf_1.png")).getImage();
        return img;
    }
    public Image createChartFrameImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/chart.png")).getImage();
        return img;
    }
    public Image createTickImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/tick.png")).getImage();
        return img;
    }
    public Image createNewSkimImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/newskim.png")).getImage();
        return img;
    }
    public Image createNewTestImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/newtest.png")).getImage();
        return img;
    }
    public Image createNewAssignImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/newassign.png")).getImage();
        return img;
    }
    public Image createNewRegImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/newreg.png")).getImage();
        return img;
    }
    public Image smartEditorMark(){
        Image img = new ImageIcon(this.getClass().getResource("resources/mark.png")).getImage();
        return img;
    }
    public Image createMaxImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/max_.png")).getImage();
        return img;
    }
    public Image createMinImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/min_.png")).getImage();
        return img;
    }
    public Image createNoneImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/none.png")).getImage();
        return img;
    }
    public Image createToggleImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/toggle1.png")).getImage();
        return img;
    }
    public Image createChatImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/chat_burned.png")).getImage();
        return img;
    }
    public Image createSigmaImage(){
        Image img = new ImageIcon(this.getClass().getResource("resources/sigma_burned.png")).getImage();
        return img;
    }
    
}

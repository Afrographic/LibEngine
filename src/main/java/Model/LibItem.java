/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author X M G
 */
public class LibItem {

    public int libItemId;
    public String itemIcon;
    public String itemName;
    public String itemAuthor;
    public String position;
    public String itemType;
    public int stock;

    public LibItem(int libItemId, String itemIcon, String itemName, String itemAuthor, int stock, String position,String itemType) {
        this.libItemId = libItemId;
        this.itemAuthor = itemAuthor;
        this.itemName = itemName;
        this.stock = stock;
        this.itemType = itemType;
        this.itemIcon = itemIcon;
        this.position = position;
    }

}

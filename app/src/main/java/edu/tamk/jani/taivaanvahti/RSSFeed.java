package edu.tamk.jani.taivaanvahti;

/**
 * Created by Jani on 12.5.2015.
 * Original: http://www.ibm.com/developerworks/xml/tutorials/x-androidrss/x-androidrss.html
 */
import java.util.List;
import java.util.Vector;
import edu.tamk.jani.taivaanvahti.RSSItem;

public class RSSFeed
{
    private String _title = null;
    private String _pubdate = null;
    private int _itemcount = 0;
    private List<RSSItem> _itemlist;

    RSSFeed()
    {
        _itemlist = new Vector(0);
    }
    int addItem(RSSItem item) {
        _itemlist.add(item);
        _itemcount++;
        return _itemcount;
    }

    RSSItem getItem(int location) {

        return _itemlist.get(location);
    }
    List getAllItems()
    {
        return _itemlist;
    }
    int getItemCount()
    {
        return _itemcount;
    }
    void setTitle(String title)
    {
        _title = title;
    }
    void setPubDate(String pubdate)
    {
        _pubdate = pubdate;
    }
    String getTitle()
    {
        return _title;
    }
    String getPubDate()
    {
        return _pubdate;
    }


}
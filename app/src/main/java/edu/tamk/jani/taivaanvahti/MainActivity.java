package edu.tamk.jani.taivaanvahti;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.*;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ArrayAdapter;
import android.os.AsyncTask;

import android.widget.AdapterView.OnItemClickListener;

import java.net.URL;
import android.util.Log;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.content.Intent;


public class MainActivity extends ActionBarActivity implements OnItemClickListener {

    private RSSFeed _feed = null;
    public final String TAIVAANVAHTIFEED = "http://www.taivaanvahti.fi/observations/rss";
    public final String tag = "Taivaanvahti";

    class RetrieveFeedTask extends AsyncTask<String, Void, RSSFeed> {

        private Exception exception;

        protected RSSFeed doInBackground(String... urls) {
            try {
                URL url= new URL(urls[0]);
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser=factory.newSAXParser();
                XMLReader xmlreader=parser.getXMLReader();
                RSSHandler theRSSHandler=new RSSHandler();
                xmlreader.setContentHandler(theRSSHandler);
                InputSource is=new InputSource(url.openStream());
                xmlreader.parse(is);
                return theRSSHandler.getFeed();
            } catch (Exception e) {
                Log.e("Taivaanvahti", "doInBackground exception "+ e.toString());
                this.exception = e;
                return null;
            }

        }

        protected void onPostExecute(RSSFeed feed) {
            updateDisplay(feed);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //RSSFeed feed = getFeed(TAIVAANVAHTIFEED);
        new RetrieveFeedTask().execute(TAIVAANVAHTIFEED);
        //updateDisplay();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateDisplay(RSSFeed feed) {
        TextView feedtitle = (TextView) findViewById(R.id.feedtitle);
        TextView feedpubdate = (TextView) findViewById(R.id.feedpubdate);
        ListView itemlist = (ListView) findViewById(R.id.itemlist);


        if (feed == null)
        {
            //TODO: move this strings.xml
            feedtitle.setText("Taivaanvahti feed not available");
            return;
        }

        feedtitle.setText(feed.getTitle());
        feedpubdate.setText(feed.getPubDate());


        ArrayAdapter<RSSItem> adapter = new
                ArrayAdapter<RSSItem>(this,android.R.layout.
                simple_list_item_1,feed.getAllItems());

        itemlist.setAdapter(adapter);
        itemlist.setSelection(0);
        itemlist.setOnItemClickListener(this);
        //TODO: set this in Ascyn's post and sync it as user might update feed any time
        _feed = feed;
    }

    public void onItemClick(AdapterView parent, View v, int position, long id)
    {
        Log.i(tag,"item clicked! " + position);

        Intent itemintent = new Intent(this,ShowDescription.class);

        Bundle b = new Bundle();
        b.putString("title", _feed.getItem(position).getTitle());
        b.putString("description", _feed.getItem(position).getDescription());
        b.putString("link", _feed.getItem(position).getLink());
        b.putString("pubdate", _feed.getItem(position).getPubDate());

        itemintent.putExtra("android.intent.extra.INTENT", b);

        startActivity(itemintent);
    }

}

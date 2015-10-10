package com.wohlig.rohan.timebomb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wohlig.rohan.timebomb.db.Note;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    List<Note> allnotes =null;
    TextView display=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        display=(TextView)findViewById(R.id.displayWindow);
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

    //Add note
    public void addButtonClicked(View v) {
        Note.deleteAll(Note.class);
        Note note= new Note("Bachi", "tag1,tag2", "#000000", "", "5:15", "2015-06-05 13:13:13", "", "2015-06-01 13:13:13", "2015-06-06 13:13:13", "1");
        note.save();
        Note note1= new Note("Chammiya", "tag2,tag3", "#ff00f0", "", "20:21", "", "", "2015-06-05 13:13:13", "2015-06-01 13:13:13", "1");
        note1.save();
        Note note2= new Note("Chuck", "tag2,tag3", "#0f00ff", "", "20:21", "2015-06-05 13:13:13", "", "2015-07-05 13:13:13", "2015-06-08 13:13:13", "2");
        note2.save();
        Note note3= new Note("Attal", "tag2,tag3", "#0f00ff", "", "00:21", "2015-12-12 13:13:13", "", "2015-08-05 13:13:13", "2015-12-01 13:13:13", "3");
        note3.save();
        Note note4= new Note("Chuck", "tag2,tag3", "#0f0000", "", "09:21", "2015-06-05 13:13:13", "", "2015-06-05 13:12:13", "2015-06-02 13:13:13", "2");
        note4.save();
        Note note5=new Note("Chomya", "tag2,tag3", "#0f1000", "", "20:01", "2015-12-12 13:13:13", "", "2015-06-05 13:13:13", "2015-06-01 14:13:13", "3");
        note5.save();
    }
    public void checkTimeClicked(View v) throws ParseException {
        List<Note> allnotes = Note.findWithQuery(Note.class, "Select * from Note WHERE timebomb IS NOT NULL AND timebomb != ''");
        SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateStr=formatter.format(new Date());
        Date currentDate=formatter.parse(currentDateStr);
        for(Note currentnote:allnotes) {
            String timebomb_time = currentnote.getTimebomb();
            if(timebomb_time != null && timebomb_time != "") {
                Date timebomb = formatter.parse(timebomb_time);
                if (currentDate.compareTo(timebomb) >= 0) {
                    currentnote.setShownote("0");
                    currentnote.save();
                } else {
                    currentnote.setShownote("1");
                    currentnote.save();
                }
            }
        }
    }
    public void alphaSortClicked(View v){
        Collections.sort(allnotes,new TitleComparator());
        printNotes();
    }
    public void getAllClicked(View v){
    allnotes= Note.findWithQuery(Note.class, "Select * from Note");
        printNotes();
    }
    public void printNotes(){
        display.setText("");
        for(Note currentnote:allnotes){
            display.append(currentnote.getTitle()+" "+currentnote.getColor()+" "+currentnote.getCreationtime()+" "+currentnote.getModificationtime()+" "+currentnote.getRemindertime()+" "+currentnote.getTimebomb());
            display.append("\n");
        }
    }
    public void colorSortClicked(View v){
    Collections.sort(allnotes,new colorComparator());
        printNotes();
    }
    public void createTimeClicked(View v){
        Log.d("rohan","inside create");
    Collections.sort(allnotes,new creationTimeComparator());
     printNotes();
    }
    public void modifiedClicked(View v){
     Collections.sort(allnotes,new modifiedTimeComparator());
        printNotes();
    }
    public void remindClicked(View v){
        Collections.sort(allnotes,new remindComparator());
        printNotes();

    }
    public void timebombClicked(View v){
    Collections.sort(allnotes,new timebombComparator());
        printNotes();
    }
    class TitleComparator implements Comparator<Note> {

        public int compare(Note c1, Note c2) {
            Log.d("rohan","difference "+(c1.getTitle().compareTo(c2.getTitle())));
            return c1.getTitle().compareTo(c2.getTitle());
        }
    }
    class colorComparator implements Comparator<Note> {

        public int compare(Note c1, Note c2) {
            return c1.getColor().compareTo(c2.getColor());
        }
    }
    class remindComparator implements Comparator<Note> {

        public int compare(Note c1, Note c2) {
            return c1.getRemindertime().compareTo(c2.getRemindertime());
        }
    }
    class creationTimeComparator implements Comparator<Note> {

        public int compare(Note c1, Note c2) {
            try{
                SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String creationtime1 = c1.getCreationtime();
                Log.d("rohan", creationtime1);
                Date creation1 = formatter.parse(creationtime1);
                String creationtime2 = c2.getCreationtime();
                Log.d("rohan", creationtime2);
                Date creation2 = formatter.parse(creationtime2);
                Log.d("rohan","difference "+(creationtime1.compareTo(creationtime2)));
                return creationtime2.compareTo(creationtime1);
            }catch (Exception e) {
            }
            finally {
            return c1.getCreationtime().compareTo(c2.getCreationtime());
            }
        }
    }
    class modifiedTimeComparator implements Comparator<Note> {

        public int compare(Note c1, Note c2) {
            try{
                SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String modifiedtime1 = c1.getModificationtime();
                Date modification1 = formatter.parse(modifiedtime1);
                String modifiedtime2 = c2.getCreationtime();
                Date modification2 = formatter.parse(modifiedtime2);
                return modification1.compareTo(modification2);
            }
            catch (Exception e) {
            }
            finally {
            return c2.getModificationtime().compareTo(c1.getModificationtime());
            }
        }
    }
    class timebombComparator implements Comparator<Note> {

        public int compare(Note c1, Note c2) {
            try{
                SimpleDateFormat formatter  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String timebomb1 = c1.getModificationtime();
                Date timebombdate1 = formatter.parse(timebomb1);
                String timebomb2 = c2.getCreationtime();
                Date timebombdate2 = formatter.parse(timebomb2);
                return timebombdate1.compareTo(timebombdate2);
            }catch (Exception e) {

            }
            finally {
            return c2.getTimebomb().compareTo(c1.getTimebomb());
            }
        }
    }

}

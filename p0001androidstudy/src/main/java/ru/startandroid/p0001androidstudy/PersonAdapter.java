package ru.startandroid.p0001androidstudy;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.List;

import ru.startandroid.p0001androidstudy.model.Person;

/**
 * @author Andrew on 2/4/2016.
 */

public class PersonAdapter extends BaseAdapter {

    public static final int IMAGE_WIDTH = 100;
    public static final int IMAGE_HEIGHT = 150;
    private final static String TAG = PersonAdapter.class.getSimpleName();
    private LayoutInflater lInflater;
    private SparseArray<Person> persons = new SparseArray<>();
    private Context context;

    public PersonAdapter(Context ctx, List<Person> persons) {
        this.context = ctx;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Person person : persons) {
            this.persons.put((int) person.id, person);
        }
    }

    @Override
    public int getCount() {
        return persons.size();
    }

    @Nullable
    @Override
    public Object getItem(int position) {
        int key = persons.keyAt(position);
        return key != -1 ? persons.get(key) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public long getPosition(Person person) {
        int position = -1;
        for (int i = 0, nsize = persons.size(); i < nsize; i++) {
            Person obj = persons.valueAt(i);
            if (obj.id == person.id) {
                position = i;
                break;
            }
        }
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = lInflater.inflate(R.layout.itemrow, parent, false);
        }

        if ((position % 2) == 1) {
            view.setBackgroundColor(Color.WHITE);
        } else {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreyMy));
        }

        if (view.isActivated()) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlightMy));
        }

        Person p = getPerson(position);

        if (p != null) {
            TextView personNameView = (TextView) view.findViewById(R.id.tvPersonName);
            TextView emailView = (TextView) view.findViewById(R.id.tvPersonEmail);
            personNameView.setText(p.name);
            emailView.setText(p.email);
            ImageView personFace = (ImageView) view.findViewById(R.id.ivImage);

            if (p.fileName != null && !p.fileName.isEmpty()) {
                File faceFile = new File(p.fileName);
                if (faceFile.exists()) {
                    Glide.with(context).load(faceFile).override(IMAGE_WIDTH, IMAGE_HEIGHT).into(personFace);
                } else
                    Glide.with(context).load(R.drawable.no_img).override(IMAGE_WIDTH, IMAGE_HEIGHT).centerCrop().into(personFace);
            } else {
                Glide.with(context).load(R.drawable.no_img).override(IMAGE_WIDTH, IMAGE_HEIGHT).centerCrop().into(personFace);
            }
        }
        return view;
    }

    public void update(@Nullable Person person) {
        if (person != null && persons.size() > person.id) {
            persons.put((int) person.id, person);
        }
    }

    public void refreshPersons(List<Person> persons) {
        this.persons.clear();
        for (Person person : persons) {
            this.persons.put((int) person.id, person);
        }
        notifyDataSetChanged();

    }

    @Nullable
    private Person getPerson(int position) {
        return ((Person) getItem(position));
    }
}
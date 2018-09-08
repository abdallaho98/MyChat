package com.dz.abdallah.mychat;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Abdallah on 25/12/2017.
 */

public class MessageListAdapter extends BaseAdapter {
    private Context context;
    private List<Message> list;

    public MessageListAdapter(Context context, List<Message> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context,R.layout.message,null);
        TextView Msg = (TextView) v.findViewById(R.id.Msg);
        TextView Usr = (TextView) v.findViewById(R.id.User);
        TextView tim = (TextView) v.findViewById(R.id.time);
        Msg.setText(list.get(position).getMsg());
        Usr.setText(list.get(position).getUser());
        tim.setText(list.get(position).getTime().toString());
        //v.setTag(list.get(position).getTime());
        return v;
    }

    public void Add(Message msg)
    {
        list.add(msg);
    }


}

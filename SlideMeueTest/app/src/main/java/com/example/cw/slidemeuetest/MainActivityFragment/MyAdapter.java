package com.example.cw.slidemeuetest.MainActivityFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cw.slidemeuetest.PostContent.PostActivity;
import com.example.cw.slidemeuetest.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.zzhoujay.richtext.RichText;

import java.util.List;

/**
 * Created by cw on 2016/11/28.
 */

public class MyAdapter extends BaseAdapter {

    private List<ItemBean> mList;

    private LayoutInflater mInflater;

    public MyAdapter(Context context,List<ItemBean> list){
        mList = list;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if(view == null){

            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.discuss_listview_item,null);

            viewHolder.name = (TextView)view.findViewById(R.id.id_TvDiscussName);
//            viewHolder.content = (MarkedView)view.findViewById(R.id.id_TvDiscussContent);
            viewHolder.content = (TextView)view.findViewById(R.id.id_TvDiscussContent);
            viewHolder.imageView = (SimpleDraweeView)view.findViewById(R.id.id_IMGhead);
            viewHolder.title = (TextView)view.findViewById(R.id.id_TvDiscussTitle);
            //viewHolder.contentimg = (SimpleDraweeView)view.findViewById(R.id.id_IMGcontent);
            viewHolder.times = (TextView)view.findViewById(R.id.id_TvDiscussTime);
            viewHolder.linearLayout = (LinearLayout)view.findViewById(R.id.id_lltoonepost);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
//            if(viewHolder.content!=null&&!viewHolder.content.equals("")){
//                viewHolder.content.setVisibility(View.GONE);
//            }
        }


        final ItemBean bean = mList.get(i);
//        viewHolder.content.setTag(bean.ItemContent);
        viewHolder.name.setText(bean.ItemName);
        //viewHolder.content.setMDText(bean.ItemContent);
//        if(viewHolder.content.getTag().equals(bean.ItemContent)){
//
//            viewHolder.content.setMDText(bean.ItemContent);
//        }
        RichText.from(bean.ItemContent).into(viewHolder.content);
        //viewHolder.content.setMDText("正在加载中...");

//        if(viewHolder.content.getTag()!=null&&viewHolder.content.getTag().equals(bean.ItemContent)){
//            viewHolder.content.setMDText(bean.ItemContent);
//        }

        Fresco.initialize(view.getContext());

        viewHolder.imageView.setImageURI(bean.getUserImgUrl());
        viewHolder.title.setText(bean.ItemTitle);
        viewHolder.times.setText(bean.ItemCreatTime);
        //viewHolder.contentimg.setImageURI(bean.getItemContentImg());
//        if(bean.getItemContentImg().contains("http://lsuplus.top/uploads/")){
//            //viewHolder.contentimg.setAspectRatio(1.62f);
//            viewHolder.contentimg.setImageURI(bean.getItemContentImg());
//
//        }

        RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
        roundingParams.setBorder(R.color.colorWhite, (float) 1.0);
        roundingParams.setRoundAsCircle(true);
        viewHolder.imageView.getHierarchy().setRoundingParams(roundingParams);

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"head",Toast.LENGTH_LONG).show();

            }
        });

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences("postInfo",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("maintitle",bean.ItemTitle);
                editor.putString("postone",bean.ItemContent);
                editor.putInt("postid",bean.getId());
                editor.putString("userheadimg",bean.getUserImgUrl());
                editor.putString("username",bean.ItemName);
                editor.putString("creattime",bean.ItemCreatTime);
                editor.commit();

                Intent intent = new Intent(view.getContext(), PostActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        return view;
    }

    class ViewHolder{
        public TextView name;
//        public MarkedView content;
        public TextView content;

        public SimpleDraweeView imageView;
        public TextView title;
        //public SimpleDraweeView contentimg;
        public TextView times;

        public LinearLayout linearLayout;
    }
}

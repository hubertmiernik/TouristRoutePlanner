package com.example.touristrouteplanner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.touristrouteplanner.model.Route;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RouteRecyclerViewAdapter extends RecyclerView.Adapter<RouteRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Route> routeList;

    public RouteRecyclerViewAdapter(Context context, List<Route> routes) {
        this.context = context;
        routeList = routes;
    }

    public RouteRecyclerViewAdapter(Context context) {
        this.context = context;
        routeList = new ArrayList<>();
    }

    public void setRouteList(List<Route> routeList) {
        this.routeList = routeList;
        notifyDataSetChanged();
    }



    @Override
    public RouteRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.route_row, parent, false);


        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteRecyclerViewAdapter.ViewHolder holder, int position) {

        Route route = routeList.get(position);
        String pictureLink = route.getPicture();

        holder.name.setText(route.getName());
        holder.region.setText(route.getRegion());
        holder.difficulty.setText(route.getDifficulty());
        holder.length.setText(route.getLength());

        Picasso.with(context).load(pictureLink).placeholder(android.R.drawable.ic_btn_speak_now).into(holder.picture);


    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView region;
        TextView difficulty;
        TextView length;
        ImageView picture;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            name = itemView.findViewById(R.id.routeNameID);
            region = itemView.findViewById(R.id.routeRegionID);
            picture = itemView.findViewById(R.id.routeImageID);
            length =  itemView.findViewById(R.id.routeLengthID);
            difficulty = itemView.findViewById(R.id.routeDifficultyID);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Route route = routeList.get(getAdapterPosition());
                    Intent intent = new Intent(context, RouteDetailActivity.class);
                    intent.putExtra("route", route);
                    ctx.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}

package com.example.touristrouteplanner;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class FilterRecyclerViewAdapter extends RecyclerView.Adapter<FilterRecyclerViewAdapter.ViewHolder>{


    private Context context;
    private List<Route> routeList;

    public FilterRecyclerViewAdapter(Context context, List<Route> routes) {
        this.context = context;
        routeList = routes;
    }


    @Override
    public FilterRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.filter_route_row, parent, false);


        return new FilterRecyclerViewAdapter.ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterRecyclerViewAdapter.ViewHolder holder, int position) {

        Route route = routeList.get(position);

        holder.name.setText(route.getName());
        holder.region.setText(route.getRegion());
        holder.difficulty.setText(route.getDifficulty());



    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView name;
        TextView region;
        TextView difficulty;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            name =  itemView.findViewById(R.id.routeNameID2);
            region =  itemView.findViewById(R.id.routeRegionID2);
            difficulty = itemView.findViewById(R.id.routeDifficultyID2);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Route route = routeList.get(getAdapterPosition());
                    Intent intent = new Intent(context, DirectionActivity.class);
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

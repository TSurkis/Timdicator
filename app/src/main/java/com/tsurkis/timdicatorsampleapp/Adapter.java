package com.tsurkis.timdicatorsampleapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.AdapterViewHolder> {


  private List<String> colors;

  Adapter(List<String> colors) {
    this.colors = colors;
  }

  @NonNull
  @Override
  public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
    View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_page, viewGroup, false);
    return new AdapterViewHolder(rootView);
  }

  @Override
  public void onBindViewHolder(@NonNull AdapterViewHolder adapterViewHolder, int position) {
    adapterViewHolder.bind(colors.get(position));
  }

  @Override
  public int getItemCount() {
    return colors.size();
  }

  static class AdapterViewHolder extends RecyclerView.ViewHolder {

    private TextView colorTextView;
    private ViewGroup container;

    AdapterViewHolder(@NonNull View itemView) {
      super(itemView);
      colorTextView = itemView.findViewById(R.id.color_text_view);
      container = itemView.findViewById(R.id.container);
    }

    private void bind(String color) {
      colorTextView.setText(color);
      container.setBackgroundColor(Color.parseColor(color));
    }
  }
}

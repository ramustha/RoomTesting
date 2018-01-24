package com.ramusthastudio.roomtesting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import java.lang.ref.WeakReference;

public final class RecyclerSupport implements
    RecyclerView.OnChildAttachStateChangeListener, View.OnClickListener {
  private final WeakReference<RecyclerClickCallback> fRef;
  private final RecyclerView fRecyclerView;

  RecyclerSupport(RecyclerView aFRecyclerView, RecyclerClickCallback aFRef) {
    fRecyclerView = aFRecyclerView;
    fRef = new WeakReference<>(aFRef);

    fRecyclerView.addOnChildAttachStateChangeListener(this);
  }

  @Override
  public void onChildViewAttachedToWindow(final View view) {
    view.setOnClickListener(this);
  }

  @Override
  public void onChildViewDetachedFromWindow(final View view) {
    view.setOnClickListener(null);
  }

  @Override
  public void onClick(final View v) {
    final RecyclerView.ViewHolder holder = fRecyclerView.getChildViewHolder(v);
    fRef.get().onClick(holder);
  }

  public final void detach() {
    fRef.clear();
    fRecyclerView.removeOnChildAttachStateChangeListener(this);
  }
}

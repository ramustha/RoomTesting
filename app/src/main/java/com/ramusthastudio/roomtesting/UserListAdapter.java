package com.ramusthastudio.roomtesting;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ramusthastudio.roomtesting.model.User;
import java.util.List;
import java.util.Objects;

public final class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListViewHolder> {
  private final Context fContext;
  private List<User> fUserList;

  public UserListAdapter(final Context aFContext) {
    fContext = aFContext;
  }

  @Override public int getItemCount() { return fUserList == null ? 0 : fUserList.size(); }

  @Override
  public UserListViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
    final View view = LayoutInflater.from(fContext).inflate(R.layout.user_item, parent, false);
    return new UserListViewHolder(view);
  }

  @Override
  public void onBindViewHolder(final UserListViewHolder holder, final int position) {
    holder.bind(fUserList.get(position));
  }

  public UserListAdapter setUserList(final List<User> aUserList) {
    if (fUserList == null) {
      fUserList = aUserList;
      notifyItemRangeInserted(0, aUserList.size());
    } else {
      DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
        @Override public int getOldListSize() { return fUserList.size(); }
        @Override public int getNewListSize() { return aUserList.size(); }
        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
          return fUserList.get(oldItemPosition).getUid() == aUserList.get(newItemPosition).getUid();
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
          User newUser = aUserList.get(newItemPosition);
          User oldUser = fUserList.get(oldItemPosition);

          return newUser.getUid() == oldUser.getUid()
              && Objects.equals(newUser.getFirstName(), oldUser.getFirstName())
              && Objects.equals(newUser.getLastName(), oldUser.getLastName());
        }
      });
      fUserList = aUserList;
      result.dispatchUpdatesTo(this);
    }
    return this;
  }

  public User getUser(final int aPosition) {
    if (fUserList != null) {
      return fUserList.get(aPosition);
    }

    throw new NullPointerException("Userlist cant be null");
  }

  static class UserListViewHolder extends RecyclerView.ViewHolder {
    private final TextView fFirstName;
    private final TextView fLastName;

    UserListViewHolder(final View itemView) {
      super(itemView);
      fFirstName = itemView.findViewById(R.id.item_first_name);
      fLastName = itemView.findViewById(R.id.item_last_name);
    }

    void bind(User aUser) {
      fFirstName.setText(aUser.getFirstName());
      fLastName.setText(aUser.getLastName());
    }
  }
}

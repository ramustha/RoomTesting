package com.ramusthastudio.roomtesting;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.viewmodel.UserListViewModel;

public class RecyclerListFragment extends Fragment implements RecyclerClickCallback {
  public static final String TAG = "UserListFragment";
  private TextView fLoadingText;
  private RecyclerView fUserListView;
  private RecyclerSupport fRecyclerSupport;
  private UserListAdapter fUserListAdapter;

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.user_fragment, container, false);
    fLoadingText = view.findViewById(R.id.loading_tv);
    fUserListView = view.findViewById(R.id.products_list);

    fUserListAdapter = new UserListAdapter(getContext());
    fRecyclerSupport = new RecyclerSupport(fUserListView, this);
    fUserListView.setLayoutManager(new LinearLayoutManager(getActivity()));
    fUserListView.setAdapter(fUserListAdapter);

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final UserListViewModel viewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
    subscribeUi(viewModel);
  }

  @Override
  public void onClick(final RecyclerView.ViewHolder aUser) {
    if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
      final User user = fUserListAdapter.getUser(aUser.getAdapterPosition());
      ((MainActivity) getActivity()).show(user);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    fRecyclerSupport.detach();
  }

  private void subscribeUi(final UserListViewModel aViewModel) {
    aViewModel.getUsers().observe(this, aUsers -> {
      if (aUsers != null) {
        fLoadingText.setVisibility(View.GONE);
        fUserListAdapter.setUserList(aUsers);
      } else {
        fLoadingText.setVisibility(View.VISIBLE);
      }
    });
  }
}

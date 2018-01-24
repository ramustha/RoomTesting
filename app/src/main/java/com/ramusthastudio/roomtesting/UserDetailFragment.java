package com.ramusthastudio.roomtesting;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ramusthastudio.roomtesting.model.User;
import com.ramusthastudio.roomtesting.viewmodel.Injection;
import com.ramusthastudio.roomtesting.viewmodel.UserDetailViewModel;
import com.ramusthastudio.roomtesting.viewmodel.ViewModelFactory;

public class UserDetailFragment extends Fragment {
  public static final String TAG = "UserDetailFragment";
  private static final String KEY_USER_ID = "user_id";
  private TextView fFirstName;
  private TextView fLastName;

  @Override
  public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.user_detail_fragment, container, false);
    fFirstName = view.findViewById(R.id.item_first_name);
    fLastName = view.findViewById(R.id.item_last_name);

    return view;
  }

  @Override
  public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    final ViewModelFactory viewModelFactory = Injection.provideViewModelFactory(getActivity().getApplication());
    final UserDetailViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserDetailViewModel.class);
    subscribeUi(viewModel.loadAllById(getArguments().getInt(KEY_USER_ID)));
  }

  private void subscribeUi(final LiveData<User> aUserLiveData) {
    aUserLiveData.observe(this, aUsers -> {
      if (aUsers != null) {
        fFirstName.setText(aUsers.getFirstName());
        fLastName.setText(aUsers.getLastName());
      } else {
        fFirstName.setText(null);
        fLastName.setText(null);
      }
    });
  }

  public static UserDetailFragment forUser(int aUid) {
    UserDetailFragment fragment = new UserDetailFragment();
    Bundle args = new Bundle();
    args.putInt(KEY_USER_ID, aUid);
    fragment.setArguments(args);
    return fragment;
  }
}

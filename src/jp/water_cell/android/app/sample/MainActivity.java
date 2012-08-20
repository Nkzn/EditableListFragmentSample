package jp.water_cell.android.app.sample;

import java.util.ArrayList;
import java.util.List;

import jp.water_cell.android.lib.EditableListFragment;
import jp.water_cell.android.lib.EditableListFragment.OnListChangedListener;
import jp.water_cell.android.lib.SimpleListItem;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.support.v4.view.Menu;

public class MainActivity extends FragmentActivity implements OnListChangedListener, LoaderCallbacks<List<SimpleListItem>> {

	ProgressDialog mSendDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ArrayList<SimpleListItem> list = new ArrayList<SimpleListItem>();
		list.add(new SimpleListItem("1", "hoge"));
		list.add(new SimpleListItem("2", "fuga"));
		list.add(new SimpleListItem("3", "piyo"));
		list.add(new SimpleListItem("4", "foo"));
		list.add(new SimpleListItem("5", "bar"));
		list.add(new SimpleListItem("6", "buz"));
		list.add(new SimpleListItem("14", "foo"));
		list.add(new SimpleListItem("51", "bar"));
		list.add(new SimpleListItem("61", "buz"));
		list.add(new SimpleListItem("42", "foo"));
		list.add(new SimpleListItem("53", "bar"));
		list.add(new SimpleListItem("56", "buz"));

		Bundle args = new Bundle();
		args.putParcelableArrayList(SimpleListItem.KEY, list);

		EditableListFragment fragment = new EditableListFragment();
		fragment.setArguments(args);
		fragment.setOnListChangedListener(this);

		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(R.id.fl_list, fragment, "hoge_first");
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onListChanged(List<SimpleListItem> items, String tag, int editType, SimpleListItem modifiedItem) {

		Bundle args = new Bundle();
		args.putInt("edit_type", editType);
		args.putParcelableArrayList("send_items", (ArrayList<SimpleListItem>) items);

		getSupportLoaderManager().initLoader(tag.hashCode(), args, this);
	}

	@Override
	public Loader<List<SimpleListItem>> onCreateLoader(int id, Bundle args) {

		final int loaderEditType = args.getInt("edit_type");
		final List<SimpleListItem> sendItems = args.getParcelableArrayList("send_items");

		if (mSendDialog == null) {
			mSendDialog = new ProgressDialog(this);
		}
		mSendDialog.setTitle("更新中です");
		mSendDialog.setMessage("しばらくお待ちください...");
		mSendDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mSendDialog.show();

		return new AsyncEditableListLoader(this, sendItems, loaderEditType);
	}

	@Override
	public void onLoadFinished(Loader<List<SimpleListItem>> loader, List<SimpleListItem> data) {

		EditableListFragment fragment = null;

		if (loader.getId() == "hoge_first".hashCode()) {
			fragment = (EditableListFragment) getSupportFragmentManager().findFragmentByTag("hoge_first");
		}

		if (data == null) {
			fragment.canceled();
		} else {
			fragment.performed(data);
		}

		if (mSendDialog != null && mSendDialog.isShowing()) {
			mSendDialog.dismiss();
		}

		getSupportLoaderManager().destroyLoader(loader.getId());
	}

	@Override
	public void onLoaderReset(Loader<List<SimpleListItem>> loader) {
		getSupportLoaderManager().destroyLoader(loader.getId());
	}

}

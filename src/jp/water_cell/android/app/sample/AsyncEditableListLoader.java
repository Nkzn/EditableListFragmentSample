package jp.water_cell.android.app.sample;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import jp.water_cell.android.lib.EditableListFragment;
import jp.water_cell.android.lib.SimpleListItem;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class AsyncEditableListLoader extends AsyncTaskLoader<List<SimpleListItem>> {

	List<SimpleListItem> mItems;

	List<SimpleListItem> result;

	int mEditType;

	public AsyncEditableListLoader(Context context, List<SimpleListItem> items, int editType) {
		super(context);
		Log.d("loader", "AsyncEditableListLoader(context, sendItems:" + items + ", editType:" + editType + ")");
		mItems = items;
		mEditType = editType;
	}

	@Override
	public List<SimpleListItem> loadInBackground() {

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// 中身はそのままコピーして返り値を作成
		List<SimpleListItem> ret = new ArrayList<SimpleListItem>(mItems);

		switch (mEditType) {
		case EditableListFragment.ADD:
			return ret;
		case EditableListFragment.EDIT:
			return ret;
		case EditableListFragment.DEL:
			return ret;
		case EditableListFragment.SORT:
			return ret;
		}

		return null;
	}

	@Override
	public void deliverResult(List<SimpleListItem> data) {
		if (isReset()) {
			if (this.result != null) {
				this.result = null;
			}
			return;
		}

		this.result = data;

		if (isStarted()) {
			super.deliverResult(data);
		}
	}

	@Override
	protected void onStartLoading() {
		if (this.result != null) {
			deliverResult(this.result);
		}
		if (takeContentChanged() || this.result == null) {
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		super.onStopLoading();
		cancelLoad();
	}

	@Override
	protected void onReset() {
		super.onReset();
		onStopLoading();
	}

	@Override
	public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
		super.dump(prefix, fd, writer, args);
		writer.print(prefix);
		writer.print("result=");
		writer.println(this.result);
	}
}

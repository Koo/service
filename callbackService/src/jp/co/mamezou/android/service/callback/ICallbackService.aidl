package jp.co.mamezou.android.service.callback;

import jp.co.mamezou.android.service.callback.ICallbackListener;

interface ICallbackService {
	void addListener(ICallbackListener listener);
	void removeListener(ICallbackListener listener);
}
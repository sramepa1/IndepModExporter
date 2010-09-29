import com.jidesoft.rss.FeedEvent;
import com.jidesoft.rss.FeedEventListener;
import com.jidesoft.status.LabelStatusBarItem;
import com.jidesoft.status.ProgressStatusBarItem;
import com.jidesoft.status.StatusBarItem;

class DemoFeedsListener implements FeedEventListener {

    StatusBarItem _barItem = null;

    DemoFeedsListener(StatusBarItem item) {
        _barItem = item;
    }

    public void eventHappened(final FeedEvent e) {
        if (e.getID() == FeedEvent.STATUS_CHANGED) {
            if (_barItem instanceof LabelStatusBarItem) {
                ((LabelStatusBarItem) _barItem).setText(e.getMessage());
            }
            if (_barItem instanceof ProgressStatusBarItem) {
                ((ProgressStatusBarItem) _barItem).setStatus(e.getMessage());
            }
        }
    }
}

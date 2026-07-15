package ua.gomin.messenger.ai;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.MessageObject;
import org.telegram.messenger.MessagesStorage;
import org.telegram.messenger.Utilities;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;
import org.telegram.sqlite.SQLiteCursor;
import org.telegram.messenger.UserConfig;

import java.util.ArrayList;
import java.util.Locale;

public class GominMessagesStorageHelper {

    public static void getMessagesForGominShield(int currentAccount, long dialogId, int count, Utilities.Callback<ArrayList<MessageObject>> callback) {
        MessagesStorage messagesStorage = MessagesStorage.getInstance(currentAccount);
        messagesStorage.getStorageQueue().postRunnable(() -> {
            ArrayList<MessageObject> messages = new ArrayList<>();
            try {
                SQLiteCursor cursor = messagesStorage.getDatabase().queryFinalized("SELECT data, mid FROM messages_v2 WHERE uid = ? ORDER BY date DESC, mid DESC LIMIT ?", new Object[]{dialogId, count});
                while (cursor.next()) {
                    NativeByteBuffer data = cursor.byteBufferValue(0);
                    if (data != null) {
                        TLRPC.Message message = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);
                        message.readAttachPath(data, UserConfig.getInstance(currentAccount).getClientUserId());
                        data.reuse();
                        if (message != null) {
                            message.dialog_id = dialogId;
                            messages.add(new MessageObject(currentAccount, message, false, true));
                        }
                    }
                }
                cursor.dispose();
            } catch (Exception e) {
                FileLog.e(e);
            }
            AndroidUtilities.runOnUIThread(() -> callback.run(messages));
        });
    }
}

package ua.gomin.messenger.hooks;

import org.telegram.SQLite.SQLiteCursor;
import org.telegram.tgnet.NativeByteBuffer;
import org.telegram.tgnet.TLRPC;

import java.util.ArrayList;

/**
 * Хуки Ghost-видалення: зберігати "видалені" повідомлення співрозмовника (анти-видалення).
 * Логіка живе тут; у коді Telegram — лише однорядковий виклик.
 */
public class GominDeleteHooks {

    public static final GominDeleteHooks INSTANCE = new GominDeleteHooks();

    /** Чи треба зберігати повідомлення як "видалене" (ghost) замість фактичного видалення. */
    public boolean shouldKeepAsGhost(boolean localDelete, boolean outgoing) {
        return GominFeatureHooks.INSTANCE.shouldKeepDeleted() && !localDelete && !outgoing;
    }

    /**
     * Позначає повідомлення як ghost (прапорець 0x40000000) і додає його в messagesToGhost.
     * Повертає true, якщо повідомлення реально оброблене як ghost (тоді викликач НЕ повинен його видаляти).
     */
    public boolean markAsGhost(SQLiteCursor cursor, ArrayList<TLRPC.Message> messagesToGhost, boolean localDelete) throws org.telegram.SQLite.SQLiteException {
        if (!shouldKeepAsGhost(localDelete, cursor.intValue(3) == 0)) {
            return false;
        }
        NativeByteBuffer data = cursor.byteBufferValue(1);
        if (data == null) {
            return false;
        }
        TLRPC.Message message = TLRPC.Message.TLdeserialize(data, data.readInt32(false), false);
        data.reuse();
        if (message == null) {
            return false;
        }
        message.flags |= 0x40000000;
        message.dialog_id = cursor.longValue(0);
        messagesToGhost.add(message);
        return true;
    }
}

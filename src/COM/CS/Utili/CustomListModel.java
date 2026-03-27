package COM.CS.Utili;

import javax.swing.*;
import java.util.AbstractList;
import java.util.ArrayList;

public class CustomListModel<T> extends DefaultListModel<T> {
    private ArrayList<T> data;
    public CustomListModel(ArrayList<T> data) {
        this.data = data;
    }

    @Override
    public T getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public int getSize() {
        return data.size();
    }

}

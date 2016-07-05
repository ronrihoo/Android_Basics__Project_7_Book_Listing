package rihoo.booklisting;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * {@link BookAdapter} for implementing Book object information into ListView.
 *
 */
public class BookAdapter extends ArrayAdapter<Book> {

    private int colorResourceId;

    /**
     * Constructor
     */
    public BookAdapter(Context context, int colorResourceId, ArrayList<Book> books) {
        super(context, 0, books);
        this.colorResourceId = colorResourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the color for the background
        int color = ContextCompat.getColor(getContext(), this.colorResourceId);

        // convertView
        View listItemView = convertView;

        // Check whether convertView is null
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.layout_list_item, parent, false);
        }

        // View/TextViews
        View textContainer = listItemView.findViewById(R.id.LL_Text_Container);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.TV_Title);
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.TV_Author);

        // Set the background color of the text container View
        textContainer.setBackgroundColor(color);

        // Book object
        Book currentBook = getItem(position);

        // Display object's information in ListView item
        titleTextView.setText(currentBook.getTitle());
        authorTextView.setText(currentBook.getAuthor());

        return listItemView;
    }
}

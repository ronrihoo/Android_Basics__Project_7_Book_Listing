package rihoo.booklisting;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    // Variables
    String title;
    String author;

    // ListView
    ListView listView;

    // Books
    ArrayList<Book> books;

    // BookAdapter
    BookAdapter bookAdapter;

    // EditText
    EditText keywordEditText;

    // Button
    Button searchButton;

    // AsyncTask Object
    FetchInfo asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ListView
        listView = (ListView) findViewById(R.id.ListView);

        // Books
        books = new ArrayList<Book>();

        // BookAdapter
        bookAdapter = new BookAdapter(this, R.color.listview_item_color, books);

        // EditText
        keywordEditText = (EditText) findViewById(R.id.ET_Keyword);

        // Button
        searchButton = (Button) findViewById(R.id.Button_Search);

        // onClickListener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = createSearchString();

                if (url != "") {
                    asyncTask = new FetchInfo(new AsyncResponse() {

                        @Override
                        public void processFinish(ArrayList<String> title, ArrayList<String> author) {
                            clearBooksList();

                            for (int i = 0; i < title.size(); i++) {
                                addBook(title.get(i), author.get(i));
                            }

                            listView.setAdapter(bookAdapter);
                        }

                    });
                    asyncTask.execute(url);
                }
            }
        });
    }

    /**
     * Add a book to the books list.
     *
     * @param title  of the book
     * @param author of the book
     */
    public void addBook(String title, String author) {
        books.add(new Book(title, author));
    }

    public void clearBooksList() {
        books.clear();
    }

    public String createSearchString() {
        String keyword = "";
        if (!keywordEditText.getText().toString().equals("")) {
            keyword = keywordEditText.getText().toString();
            Toast.makeText(MainActivity.this, "Please wait.",
                    Toast.LENGTH_SHORT).show();
            return "https://www.googleapis.com/books/v1/volumes?" + "q=" + keyword;
        } else {
            Toast.makeText(MainActivity.this, "No keyword has been provided.",
                    Toast.LENGTH_LONG).show();
            return keyword;
        }
    }

}

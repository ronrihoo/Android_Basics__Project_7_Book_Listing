package rihoo.booklisting;

/**
 * {@link Book} is used to store information about a book.
 *
 */
public class Book {

    // Variables
    private String title;
    private String author;

    /**
     * Constructor
     *
     * @param title  of the book
     * @param author of the book
     */
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
    }

    /**
     * Get the book's title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * Get the author's name.
     */
    public String getAuthor() {
        return this.author;
    }

}
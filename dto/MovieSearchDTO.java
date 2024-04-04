package dto;

public class MovieSearchDTO {
    private String genre;
    private Integer year;
    private Double minimumRating;
    private String sortBy;

    // Constructor
    public MovieSearchDTO(String genre, Integer year, Double minimumRating, String sortBy) {
        this.genre = genre;
        this.year = year;
        this.minimumRating = minimumRating;
        this.sortBy = sortBy;
    }

    // Getters
    public String getGenre() {
        return genre;
    }

    public Integer getYear() {
        return year;
    }

    public Double getMinimumRating() {
        return minimumRating;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setMinimumRating(Double minimumRating) {
        this.minimumRating = minimumRating;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }


}

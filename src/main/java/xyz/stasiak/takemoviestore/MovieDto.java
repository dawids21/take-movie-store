package xyz.stasiak.takemoviestore;

record MovieDto(long id, String title, String category, int yearOfRelease, String plotDescription,
                int price, boolean inShoppingCart) {
    static MovieDto from(Movie movie, boolean inShoppingCart) {
        return new MovieDto(movie.getId(), movie.getTitle(), movie.getCategory(), movie.getYearOfRelease(),
                movie.getPlotDescription(), movie.getPrice(), inShoppingCart);
    }
}
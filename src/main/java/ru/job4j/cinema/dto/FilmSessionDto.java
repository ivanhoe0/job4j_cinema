package ru.job4j.cinema.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class FilmSessionDto {
    private int id;

    private String film;

    private String hall;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private int rowCount;

    private int placeCount;

    private int price;

    private int fileId;

    public FilmSessionDto() {
    }

    public FilmSessionDto(int id, String film, String hall, LocalDateTime startDate, LocalDateTime endDate, int rowCount, int placeCount, int price, int fileId) {
        this.id = id;
        this.film = film;
        this.hall = hall;
        this.startDate = startDate;
        this.endDate = endDate;
        this.rowCount = rowCount;
        this.placeCount = placeCount;
        this.price = price;
        this.fileId = fileId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilm() {
        return film;
    }

    public void setFilm(String film) {
        this.film = film;
    }

    public String getHall() {
        return hall;
    }

    public void setHall(String hall) {
        this.hall = hall;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getPlaceCount() {
        return placeCount;
    }

    public void setPlaceCount(int placeCount) {
        this.placeCount = placeCount;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionDto that = (FilmSessionDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

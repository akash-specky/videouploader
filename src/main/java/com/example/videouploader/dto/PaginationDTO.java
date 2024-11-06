package com.example.videouploader.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {

    @NotNull(message = "PageNo is mandetory")
    @Min(value = 1, message = "pageNo must be greater than zero")
    Integer pageNo;

    @NotNull(message = "size is mandetory")
    @Min(value = 0, message = "size must be greater than zero")
    Integer size;



    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}

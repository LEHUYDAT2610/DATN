package vn.fs.dto;

import lombok.*;

import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {
    private BigInteger id;
    private String name;
    private String content;
    private Date dateRate;
    private int rate;
}

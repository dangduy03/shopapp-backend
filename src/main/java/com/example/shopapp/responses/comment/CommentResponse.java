package com.example.shopapp.responses.comment;

import com.example.shopapp.models.Comment;
import com.example.shopapp.responses.BaseResponse;
import com.example.shopapp.responses.user.UserResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse extends BaseResponse {
	
    @JsonProperty("id")
    private Long id;

    @JsonProperty("content")
    private String content;

    //user's information
    //@JsonProperty("user_id")
    //private Long userId;

    @JsonProperty("user")
    private UserResponse user;

    @JsonProperty("product_id")
    private Long productId;

    //@JsonProperty("created_at")
    //private LocalDateTime createdAt;
    //private String createdAt;//iso string

    public static CommentResponse fromComment(Comment comment) {
        UserResponse userResponse = UserResponse.fromUser(comment.getUser());
        CommentResponse result = CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(userResponse)
                //.userId(comment.getUser().getId())
                .productId(comment.getProduct().getId())
                //.userResponse(UserResponse.fromUser(comment.getUser()))
                //.createdAt(comment.getCreatedAt())
                .build();
        result.setCreatedAt(comment.getCreatedAt());
        return result;
    }
}

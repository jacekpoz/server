package org.gogame.server.domain.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.gogame.server.domain.entities.UserInviteStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInviteDto {

    private Long userSenderId;

    private Long userReceiverId;
}

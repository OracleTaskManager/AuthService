package com.Oracle.AuthService.data;

import java.util.Date;

public record TeamResponse(
        Long team_id,
        String team_name,
        Date created_at
) {
}

package com.act.taxaudit.api.dto.request;

import java.util.List;

public record RequestDocumentsRequest(List<String> documentTypes, String requestedByActorId) {}
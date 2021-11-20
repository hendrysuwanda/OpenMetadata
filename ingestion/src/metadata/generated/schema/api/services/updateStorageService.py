# generated by datamodel-codegen:
#   filename:  schema/api/services/updateStorageService.json
#   timestamp: 2021-11-20T15:09:34+00:00

from __future__ import annotations

from typing import Optional

from pydantic import BaseModel, Field


class UpdateStorageServiceEntityRequest(BaseModel):
    description: Optional[str] = Field(
        None, description='Description of Storage service entity.'
    )

# generated by datamodel-codegen:
#   filename:  schema/entity/policies/accessControl/rule.json
#   timestamp: 2021-11-20T15:09:34+00:00

from __future__ import annotations

from typing import List

from pydantic import BaseModel, Field

from .. import filters
from . import tagBased


class AccessControlRule(BaseModel):
    filters: filters.Filters1
    actions: List[tagBased.TagBased] = Field(
        ...,
        description='A set of access control enforcements to take on the entities.',
        min_length=1,
    )

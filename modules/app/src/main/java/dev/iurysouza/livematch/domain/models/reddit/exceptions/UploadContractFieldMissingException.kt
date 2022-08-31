package dev.iurysouza.livematch.domain.models.reddit.exceptions

class UploadContractFieldMissingException(field: String) :
    Exception("The field '$field' was not found while creating UploadData!")

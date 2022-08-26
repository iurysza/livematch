package dev.iurysouza.livematch.data.models.reddit.exceptions

class UploadContractFieldMissingException(field: String) :
    Exception("The field '$field' was not found while creating UploadData!")

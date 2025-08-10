package com.example.societas.features.example

class ExampleService {
    private val examples = mutableListOf(
        Example(1, "Example 1"),
        Example(2, "Example 2")
    )

    fun getExamples(): List<Example> {
        return examples
    }
}

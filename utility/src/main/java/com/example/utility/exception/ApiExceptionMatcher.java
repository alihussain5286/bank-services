/**
 * 
 */
package com.example.utility.exception;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author admin
 *
 */
public class ApiExceptionMatcher extends TypeSafeMatcher<ApiException> {
	/**
	 * The error code we're expecting.
	 */
	private String expectedErrorCode;

	/**
	 * Constructor.
	 * 
	 * @param expectedErrorCode
	 *            the error code that we're expecting
	 */
	public ApiExceptionMatcher(String expectedErrorCode)
	{
		this.expectedErrorCode = expectedErrorCode;
	}

	/**
	 * Describe the error condition.
	 */
	@Override
	public void describeTo(Description description)
	{
		description.appendText("Error code doesn't match");
	}

	/**
	 * Test if the input exception matches the expected exception.
	 */
	@Override
	protected boolean matchesSafely(ApiException exceptionToTest)
	{
		return this.expectedErrorCode.equalsIgnoreCase(exceptionToTest.getErrorCode());
	}
}

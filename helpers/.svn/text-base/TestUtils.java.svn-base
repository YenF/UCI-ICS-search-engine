package ir.assignments.three.helpers;

import static org.junit.Assert.assertEquals;

import ir.assignments.three.stats.Frequency;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestUtils {

	public static void compareFrequencyLists(List<Frequency> expected, List<Frequency> actual) {
		assertEquals(expected.size(), actual.size());

		for (int i = 0; i < expected.size(); i++) {
			Frequency expectedFreq = expected.get(i);
			Frequency actualFreq = actual.get(i);

			assertEquals(expectedFreq.getText(), actualFreq.getText());
			assertEquals(expectedFreq.getFrequency(), actualFreq.getFrequency());

		}
	}

	public static <T,K> K callPrivateStaticMethod(Class<T> type, String methodName, Object[] params) throws NoSuchMethodException, IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		// Find a method with a matching name
		Method matchingMethod = null;
		for (Method method : type.getDeclaredMethods()) {
			if (method.getName().equals(methodName)) {
				matchingMethod = method;
				break;
			}
		}

		if (matchingMethod == null)
			throw new NoSuchMethodException();

		// Call the method
		matchingMethod.setAccessible(true);
		Object result = matchingMethod.invoke(null, params);
		
		return (K)result;
	}
	
	public static File getTempFile(String content) {
		File tmpFile = null;

		try {
			tmpFile = File.createTempFile("test", ".tmp");
			FileWriter fstream = new FileWriter(tmpFile);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(content);
			// Close the output stream
			out.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return tmpFile;
	}

	public static void deleteTempFile(File file) {
		file.delete();
	}
}

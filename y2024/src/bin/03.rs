use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::{concat, Itertools};
use regex::Regex;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "03"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))";
const TEST2: &str = "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))";

fn main() -> Result<()> {
    start_day(DAY);

    fn mul_sum(line: String) -> i64 {
        let re = Regex::new(r"(mul\(([0-9]{1,3}),([0-9]{1,3})\))").unwrap();
        let line_sum: i64 = re
            .captures_iter(&*line)
            .map(|c| {
                let (_, [_, left, right]) = c.extract();
                let left_num = left.parse::<i64>().unwrap();
                let right_num = right.parse::<i64>().unwrap();

                left_num * right_num
            })
            .sum();
        line_sum
    }

    //region Part 1
    println!("=== Part 1 ===");

    fn part1<R: BufRead>(mut reader: R) -> Result<i64> {
        let mut answer = 0;
        for line in reader.lines() {
            let mut value = mul_sum(line.unwrap());
            let line_sum = value;
            answer += line_sum;
        }

        Ok(answer)
    }

    assert_eq!(161, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(mut reader: R) -> Result<i64> {
        let mut answer = 0;
        let mut single_line = String::new();
        reader
            .lines()
            .for_each(|line| single_line.push_str(&line.unwrap()));
        let newline = format!("do(){}don't()", single_line);
        let re = Regex::new(r"(?U)do\(\)(.*)don't\(\)")?;

        for mat in re.captures_iter(&*newline) {
            let (_, [fragment]) = mat.extract();
            answer += mul_sum(fragment.to_string())
        }
        Ok(answer)
    }

    assert_eq!(48, part2(BufReader::new(TEST2.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
